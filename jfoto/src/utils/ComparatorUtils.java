package utils;

import core.interfaces.Identifiable;
import core.interfaces.Nameable;

import java.util.Comparator;

public class ComparatorUtils {

	public static <T extends Identifiable> Comparator sortByIdAsc() {
		return new Comparator<T>() {
			@Override
			public int compare( T o1, T o2 ) {
				return o1.getId() - o2.getId();
			}
		};
	}

	public static <T extends Identifiable> Comparator sortByIdDesc() {
		return new Comparator<T>() {
			@Override
			public int compare( T o1, T o2 ) {
				return o2.getId() - o1.getId();
			}
		};
	}

	public static <T extends Nameable> Comparator sortByNameAsc() {
		return new Comparator<T>() {
			@Override
			public int compare( T o1, T o2 ) {
				return o1.getName().compareTo( o2.getName() );
			}
		};
	}

	public static <T extends Nameable> Comparator sortByNameDesc() {
		return new Comparator<T>() {
			@Override
			public int compare( T o1, T o2 ) {
				return o2.getName().compareTo( o1.getName() );
			}
		};
	}

}
