package core.general.base;

import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import ui.elements.PageItem;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PagingModel {

	private final static int EXTREME_LEFT_PAGES_MIN_QTY = 3;
	private final static int EXTREME_RIGHT_PAGES_MIN_QTY = 1;
	private final static int PAGE_SHOULDER = 5;

	private String requestUrl;

	private int currentPage;
	private int totalItems;
	private int itemsOnPage;

	private final Services services;

	public PagingModel( final Services services ) {
		this.services = services;
	}

	public List<PageItem> getPageItems( final Language language ) {
		final TranslatorService translatorService = services.getTranslatorService();

		final int shownPagesBunch = PAGE_SHOULDER * 2;
		final int totalPages = getTotalPages();

		final List<PageItem> pageItems = newArrayList();

		int firstBunchPageNumber = currentPage - PAGE_SHOULDER;
		int lastBunchPageNumber = currentPage + PAGE_SHOULDER;

		if ( currentPage <= shownPagesBunch - 1 ) {
			firstBunchPageNumber = 1;
			lastBunchPageNumber = shownPagesBunch + EXTREME_LEFT_PAGES_MIN_QTY + 1;
		}

		if ( currentPage > totalPages - EXTREME_RIGHT_PAGES_MIN_QTY * 2 + 1 ) {
			firstBunchPageNumber = totalPages - ( shownPagesBunch + EXTREME_RIGHT_PAGES_MIN_QTY );
			lastBunchPageNumber = totalPages;
		}

		if ( currentPage > totalPages - PAGE_SHOULDER - EXTREME_RIGHT_PAGES_MIN_QTY ) {
			firstBunchPageNumber = totalPages - shownPagesBunch - EXTREME_RIGHT_PAGES_MIN_QTY;
			lastBunchPageNumber = totalPages;
		}

		if ( firstBunchPageNumber < 1 ) {
			firstBunchPageNumber = EXTREME_LEFT_PAGES_MIN_QTY + 1;
			lastBunchPageNumber = EXTREME_LEFT_PAGES_MIN_QTY + shownPagesBunch;
		}

		if ( lastBunchPageNumber > totalPages ) {
			lastBunchPageNumber = totalPages;
		}

		addPageIfDoesNotExist( pageItems, 1 ); // add first page
		addFirstExtremePages( pageItems, totalPages );

		if ( firstBunchPageNumber > EXTREME_LEFT_PAGES_MIN_QTY + 1 ) {
			final PageItem lastSeparator = PageItem.getSeparatorPage();
			lastSeparator.setTitle( translatorService.translate( "Pages $1 - $2", language, String.valueOf( EXTREME_LEFT_PAGES_MIN_QTY + 1 ), String.valueOf( firstBunchPageNumber - 1 ) ) );

			pageItems.add( lastSeparator );
		}

		for ( int i = firstBunchPageNumber; i <= lastBunchPageNumber; i++ ) {
			addPageIfDoesNotExist( pageItems, i );
		}

		int limit = totalPages - EXTREME_RIGHT_PAGES_MIN_QTY;
		if ( lastBunchPageNumber < limit ) {
			final PageItem lastSeparator = PageItem.getSeparatorPage();
			lastSeparator.setTitle( translatorService.translate( "Pages $1 - $2", language, String.valueOf( lastBunchPageNumber + 1 ), String.valueOf( totalPages - EXTREME_RIGHT_PAGES_MIN_QTY ) ) );

			pageItems.add( lastSeparator );
		}

		if ( lastBunchPageNumber < totalPages ) {
			addLastExtremePages( pageItems, totalPages );
		}

		return pageItems;
	}

	private boolean isPageNumberAdded( final int pageNumber, final List<PageItem> pageItems ) {
		for ( final PageItem pageItem : pageItems ) {
			if ( pageItem.getNumber() == pageNumber ) {
				return true;
			}
		}
		return false;
	}

	private void addFirstExtremePages( final List<PageItem> pageItems, final int totalPages ) {
		for ( int i = 2; i <= EXTREME_LEFT_PAGES_MIN_QTY; i++ ) {
			if ( i <= totalPages ) {
				addPageIfDoesNotExist( pageItems, i );
			}
		}
	}

	private void addLastExtremePages( final List<PageItem> pageItems, final int totalPages ) {
		final int firstLeftExtremeElement = totalPages - EXTREME_RIGHT_PAGES_MIN_QTY + 1;
		for ( int i = firstLeftExtremeElement; i <= totalPages; i++ ) {
			addPageIfDoesNotExist( pageItems, i );
		}
	}

	private void addPageIfDoesNotExist( final List<PageItem> pageItems, final int i ) {
		if ( ! isPageNumberAdded( i, pageItems ) ) {
			pageItems.add( new PageItem( i ) );
		}
	}

	public int getTotalPages() {
		final float temp = ( float ) totalItems / itemsOnPage;
		return ( int ) Math.ceil( temp );
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl( final String requestUrl ) {
		this.requestUrl = requestUrl;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage( final int currentPage ) {
		this.currentPage = currentPage;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems( final int totalItems ) {
		this.totalItems = totalItems;
	}

	public int getItemsOnPage() {
		return itemsOnPage;
	}

	public void setItemsOnPage( final int itemsOnPage ) {
		this.itemsOnPage = itemsOnPage;
	}
}
