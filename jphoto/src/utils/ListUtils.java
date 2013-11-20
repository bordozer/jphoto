package utils;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import core.interfaces.Identifiable;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ListUtils {

	public static List<String> convertObjectListToString( final List<Object> integers ) {
		return Lists.transform( integers, Functions.toStringFunction() );
	}

	public static List<String> convertIntegerListToString( final List<Integer> integers ) {
		return Lists.transform( integers, Functions.toStringFunction() );
	}

	public static List<Integer> convertStringListToInteger( final List<String> strings ) {
		final Function<String, Integer> function = new Function<String, Integer>() {
			@Override
			public Integer apply( final String s ) {
				return NumberUtils.convertToInt( s );
			}

			@Override
			public boolean equals( final Object o ) {
				return false;
			}
		};

		return Lists.transform( strings, function );
	}

	public static List<Integer> convertIdentifiableListToListOfIds( final List<? extends Identifiable> objects ) {
		final List<Integer> result = newArrayList();

		for ( final Identifiable object : objects ) {
			result.add( object.getId() );
		}

		return result;
	}
}
