package core.general.base;

import core.interfaces.Identifiable;
import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringUtils;
import utils.ListUtils;
import utils.NumberUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class CommonProperty {

	private static final String PROPERTY_TRUE = "1";
	private static final String PROPERTY_FALSE = "0";

	private static final String VALUE_SEPARATOR = ",";

	private final int key;
	private final String value;

	public CommonProperty( final int key, final String value ) {
		this.key = key;
		this.value = value;
	}

	public CommonProperty( final int key, final int value ) {
		this.key = key;
		this.value = String.valueOf( value );
	}

	public CommonProperty( final int key, final long value ) {
		this.key = key;
		this.value = String.valueOf( value );
	}

	public CommonProperty( final int key, final boolean value ) {
		this.key = key;
		this.value = value ? PROPERTY_TRUE : PROPERTY_FALSE;
	}

	public CommonProperty( final int key, final List<String> values ) {
		this.key = key;

		if ( values == null ) {
			value = StringUtils.EMPTY;
			return;
		}

		final List<String> strings = newArrayList();

		for ( final String value : values ) {
			strings.add( value );
		}
		value = StringUtils.join( strings, ',' );
	}

	public CommonProperty( final int key, final Date date, final DateUtilsService dateUtilsService ) {
		this.key = key;
		this.value = dateUtilsService.formatDateTime( date );
	}

	public static CommonProperty createFromIntegerList( final int key, final List<Integer> values, final DateUtilsService dateUtilsService ) {
		final List<String> strings = ListUtils.convertIntegerListToString( values );
		return new CommonProperty( key, strings );
	}

	public static CommonProperty createFromIdentifiable( final int key, final List<? extends Identifiable> values ) {
		final String stringIds = convertListToStringProperty( values );
		return new CommonProperty( key, stringIds );
	}

	public static String convertListToStringProperty( final List<? extends Identifiable> list ) {

		final List<String> strings = newArrayList();
		for ( final Identifiable month : list ) {
			strings.add( String.valueOf( month.getId() ) );
		}
		return StringUtils.join( strings, ',' );
	}

	public int getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public int getValueInt() {
		return NumberUtils.convertToInt( value );
	}

	public long getValueLong() {
		return NumberUtils.convertToLong( value );
	}

	public float getValueFloat() {
		return NumberUtils.convertToFloat( value );
	}

	public boolean getValueBoolean() {
		return value.equals( PROPERTY_TRUE );
	}

	public List<String> getValueListString() {
		final String[] strings = value.split( VALUE_SEPARATOR );

		final List<String> result = newArrayList();
		for ( final String string : strings ) {
			result.add( StringUtils.trim( string ) );
		}

		return result;
	}

	public List<Integer> getValueListInt() {
		final List<String> strings = Arrays.asList( value.split( VALUE_SEPARATOR ) );

		final List<Integer> integers = newArrayList();
		for ( final String string : strings ) {
			integers.add( Integer.valueOf( string ) );
		}

		return integers;
	}

	public Date getValueTime( final DateUtilsService dateUtilsService) {
		if ( StringUtils.isEmpty( value ) ) {
			return null;
		}
		return dateUtilsService.parseDateTime( value );
	}

	public static CommonProperty getEmptyProperty( final int propertyId ) {
		return new CommonProperty( propertyId, StringUtils.EMPTY );
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", key, value );
	}
}
