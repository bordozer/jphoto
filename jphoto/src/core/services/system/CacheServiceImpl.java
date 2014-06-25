package core.services.system;

import com.google.common.collect.Maps;
import core.general.cache.CacheEntryFactory;
import core.general.cache.CacheKey;
import core.general.cache.keys.CacheCompositeKey;
import core.general.cache.keys.IntegerCacheKey;
import core.general.configuration.ConfigurationKey;
import core.interfaces.Cacheable;
import core.log.LogHelper;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class CacheServiceImpl<T extends Cacheable> implements CacheService<T> {

	private final Map<CacheKey, Map<CacheCompositeKey, CacheEntry>> cache = newHashMap();

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DateUtilsService dateUtilsService;

	private final LogHelper log = new LogHelper( CacheServiceImpl.class );

	public CacheServiceImpl() {
		for ( final CacheKey cacheKey : CacheKey.values() ) {
			cache.put( cacheKey, Maps.<CacheCompositeKey, CacheEntry> newHashMap() );
		}
	}

	@Override
	public T getEntry( final CacheKey key, final CacheCompositeKey compositeKey, final CacheEntryFactory<T> entryFactory ) {

		if ( !configurationService.getBoolean( ConfigurationKey.CACHE_USE_CACHE ) ) {
			return entryFactory.createEntry();
		}

		synchronized ( cache.get( key ) ) {

			if ( hasEntry( key, compositeKey ) ) {
				return updateLastAccessTimeAndReturnCachedEntry( key, compositeKey );
			}

			if ( cache.get( key ).size() >= getCacheMaxSize( key ) ) {
				removeTheOldestEntriesFromCache( key );
			}

			final T item = entryFactory.createEntry();

			if ( item == null ) {
				return null;
			}

			cache.get( key ).put( compositeKey, new CacheEntry( compositeKey, item ) );

			log.debug( String.format( "CACHE -> %s -> entry #%s has been PUT", key, compositeKey ) );

			return item;
		}
	}

	@Override
	public T getEntry( final CacheKey key, final int id, final CacheEntryFactory<T> entryFactory ) {
		return getEntry( key, new IntegerCacheKey( id ), entryFactory );
	}

	@Override
	public void expire( final CacheKey key, final CacheCompositeKey compositeKey ) {

		if ( !hasEntry( key, compositeKey ) ) {
			return;
		}

		synchronized ( cache.get( key ) ) {

			if ( !hasEntry( key, compositeKey ) ) {
				return;
			}

			if ( cache.get( key ).containsKey( compositeKey ) ) {
				cache.get( key ).remove( compositeKey );
				log.debug( String.format( "CACHE -> %s -> entry #%s has been REMOVED", key, compositeKey ) );
			}
		}
	}

	@Override
	public void expire( final CacheKey key, final int id ) {
		expire( key, new IntegerCacheKey( id ) );
	}

	@Override
	public void expire( final CacheKey key ) {
		synchronized ( cache.get( key ) ) {
			cache.get( key ).clear();
			log.debug( String.format( "CACHE -> key %s -> has been CLEARED", key ) );
		}
	}

	private boolean hasEntry( final CacheKey key, final CacheCompositeKey compositeKey ) {
		return cache.get( key ).containsKey( compositeKey );
	}

	private T updateLastAccessTimeAndReturnCachedEntry( final CacheKey key, final CacheCompositeKey compositeKey ) {

		final CacheEntry cacheEntry = cache.get( key ).get( compositeKey );

		cacheEntry.setLastAccessTime( dateUtilsService.getCurrentTime() );

		return cacheEntry.getEntry();
	}

	private void removeTheOldestEntriesFromCache( final CacheKey key ) {
		final int cacheMaxSize = getCacheMaxSize( key );

		while ( cache.get( key ).size() >= cacheMaxSize ) {
			final CacheEntry theEldestEntry = getTheEldestCacheEntry( key );

			if ( theEldestEntry != null ) {
				expire( key, theEldestEntry.getCompositeKey() );
			}
		}
	}

	private CacheEntry getTheEldestCacheEntry( final CacheKey key ) {
		CacheEntry theEldestEntry = null;

		for ( final CacheCompositeKey compositeKey : cache.get( key ).keySet() ) {

			if ( theEldestEntry == null ) {
				theEldestEntry = cache.get( key ).get( compositeKey );
				continue;
			}

			if ( cache.get( key ).get( compositeKey ).getLastAccessTime().getTime() > theEldestEntry.getLastAccessTime().getTime() ) {
				theEldestEntry = cache.get( key ).get( compositeKey );
			}
		}

		return theEldestEntry;
	}

	private class CacheEntry {

		private final CacheCompositeKey compositeKey;
		private final T entry;
		private Date lastAccessTime;

		private CacheEntry( final CacheCompositeKey compositeKey, final T entry ) {
			this.compositeKey = compositeKey;
			this.entry = entry;
			lastAccessTime = dateUtilsService.getCurrentTime();
		}

		public CacheCompositeKey getCompositeKey() {
			return compositeKey;
		}

		public T getEntry() {
			return entry;
		}

		public void setLastAccessTime( final Date lastAccessTime ) {
			this.lastAccessTime = lastAccessTime;
		}

		public Date getLastAccessTime() {
			return lastAccessTime;
		}

		@Override
		public String toString() {
			return String.format( "%s: %s, %s, %s", entry.getClass().getName(), compositeKey, entry, dateUtilsService.formatDateTime( lastAccessTime ) );
		}
	}

	private int getCacheMaxSize( final CacheKey key ) {
		switch ( key ) {
			case USER:
				return configurationService.getInt( ConfigurationKey.CACHE_LENGTH_USER );
			case USER_AVATAR:
				return configurationService.getInt( ConfigurationKey.CACHE_LENGTH_USER_AVATAR );
			case GENRE:
				return configurationService.getInt( ConfigurationKey.CACHE_LENGTH_GENRE );
			case GENRE_VOTING_CATEGORY:
				return configurationService.getInt( ConfigurationKey.CACHE_LENGTH_GENRE_VOTING_CATEGORY );
			case PHOTO:
				return configurationService.getInt( ConfigurationKey.CACHE_LENGTH_PHOTO );
			case PHOTO_COMMENT:
				return configurationService.getInt( ConfigurationKey.CACHE_LENGTH_PHOTO_COMMENT );
			case PHOTO_INFO:
				return configurationService.getInt( ConfigurationKey.CACHE_LENGTH_PHOTO_INFO );
			case PHOTO_VOTING_CATEGORY:
				return configurationService.getInt( ConfigurationKey.CACHE_LENGTH_PHOTO_VOTING_CATEGORY );
			case USER_TEAM_MEMBER:
				return configurationService.getInt( ConfigurationKey.CACHE_LENGTH_USER_TEAM_MEMBER );
			case USER_PHOTO_ALBUM:
				return configurationService.getInt( ConfigurationKey.CACHE_LENGTH_USER_PHOTO_ALBUM );
			case USER_GENRE_RANK:
				return configurationService.getInt( ConfigurationKey.CACHE_LENGTH_USER_GENRE_RANK );
			case RANK_IN_GENRE_POINTS_CACHE_ENTRY:
				return configurationService.getInt( ConfigurationKey.CACHE_LENGTH_RANK_IN_GENRE_POINTS );
			case USER_GENRE_PHOTOS_QTY:
				return configurationService.getInt( ConfigurationKey.CACHE_LENGTH_USER_PHOTOS_BY_GENRES );
		}

		throw new IllegalArgumentException( String.format( "Invalid cache key: %s", key ) );
	}

	public void setConfigurationService( final ConfigurationService configurationService ) {
		this.configurationService = configurationService;
	}

	public void setDateUtilsService( final DateUtilsService dateUtilsService ) {
		this.dateUtilsService = dateUtilsService;
	}
}
