package org.heat.shared.stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class ImmutableCollectors {
	private ImmutableCollectors() {}

	@SuppressWarnings("unchecked")
	private static final Collector<Object, ImmutableSet.Builder, ImmutableSet> SETS = new Collector<Object, ImmutableSet.Builder, ImmutableSet>() {
		@Override
		public Supplier<ImmutableSet.Builder> supplier() {
			return ImmutableSet::builder;
		}

		@Override
		public BiConsumer<ImmutableSet.Builder, Object> accumulator() {
			return ImmutableSet.Builder::add;
		}

		@Override
		public BinaryOperator<ImmutableSet.Builder> combiner() {
			return (a, b) -> a.addAll(b.build());
		}

		@Override
		public Function<ImmutableSet.Builder, ImmutableSet> finisher() {
			return ImmutableSet.Builder::build;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return ImmutableSet.of();
		}
	};

	@SuppressWarnings("unchecked")
	private static final Collector<Object, ImmutableList.Builder, ImmutableList> LISTS = new Collector<Object, ImmutableList.Builder, ImmutableList>() {
		@Override
		public Supplier<ImmutableList.Builder> supplier() {
			return ImmutableList::builder;
		}

		@Override
		public BiConsumer<ImmutableList.Builder, Object> accumulator() {
			return ImmutableList.Builder::add;
		}

		@Override
		public BinaryOperator<ImmutableList.Builder> combiner() {
			return (a, b) -> a.addAll(b.build());
		}

		@Override
		public Function<ImmutableList.Builder, ImmutableList> finisher() {
			return ImmutableList.Builder::build;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return ImmutableSet.of();
		}
	};

	private static final class Maps<T, K, V> implements Collector<T, ImmutableMap.Builder<K, V>, ImmutableMap<K, V>> {

		private final Function<T, K> toKey;
		private final Function<T, V> toValue;

		private Maps(Function<T, K> toKey, Function<T, V> toValue) {
			this.toKey = Objects.requireNonNull(toKey, "toKey");
			this.toValue = Objects.requireNonNull(toValue, "toValue");
		}

		@Override
		public Supplier<ImmutableMap.Builder<K, V>> supplier() {
			return ImmutableMap::builder;
		}

		@Override
		public BiConsumer<ImmutableMap.Builder<K, V>, T> accumulator() {
			return (a, b) -> a.put(toKey.apply(b), toValue.apply(b));
		}

		@Override
		public BinaryOperator<ImmutableMap.Builder<K, V>> combiner() {
			return (a, b) -> a.putAll(b.build());
		}

		@Override
		public Function<ImmutableMap.Builder<K, V>, ImmutableMap<K, V>> finisher() {
			return ImmutableMap.Builder::build;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return ImmutableSet.of();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Collector<T, ?, ImmutableSet<T>> toSet() {
		return (Collector) SETS;
	}

	@SuppressWarnings("unchecked")
	public static <T> Collector<T, ?, ImmutableList<T>> toList() {
		return (Collector) LISTS;
	}

	public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toMap(Function<T, K> toKey, Function<T, V> toValue) {
		return new Maps<>(toKey, toValue);
	}

	public static <T, K> Collector<T, ?, ImmutableMap<K, T>> toMap(Function<T, K> toKey) {
		return ImmutableCollectors.<T, K, T>toMap(toKey, Function.identity());
	}

	public static <K, V, T extends Map.Entry<K, V>> Collector<T, ?, ImmutableMap<K, V>> toMap() {
		return toMap(T::getKey, T::getValue);
	}
}
