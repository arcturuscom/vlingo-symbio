// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.symbio.store.object;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.vlingo.symbio.store.object.ObjectStoreReader.QueryMode;

/**
 * A query expression whose parameters are provided in a {@code Map} of name-value pairs.
 */
public class MapQueryExpression extends QueryExpression {
  public final Map<String,?> parameters;

  /**
   * Answer a new {@code FluentMap<String,Object>} with a single {@code key} and {@code value}.
   * @param key the String key
   * @param value the Object value
   * @return {@code FluentMap<String,Object>}
   */
  public static FluentMap<String,Object> map(final String key, final Object value) {
    final FluentMap<String,Object> map = new FluentMap<>(1);
    map.put(key, value);
    return map;
  }

  /**
   * Answer a new {@code MapQueryExpression} with {@code type}, {@code query}, and {@code parameters}.
   * @param type the {@code Class<?>} of the objects to be queried
   * @param query the String describing the query
   * @param parameters the {@code Map<String,?>} containing query parameters of name-value pairs
   * @return MapQueryExpression
   */
  public static MapQueryExpression using(final Class<?> type, final String query, final Map<String,?> parameters) {
    return new MapQueryExpression(type, query, parameters);
  }

  /**
   * Answer a new {@code MapQueryExpression} with {@code type}, {@code query}, {@code mode}, and {@code parameters}.
   * @param type the {@code Class<?>} of the objects to be queried
   * @param query the String describing the query
   * @param mode the QueryMode
   * @param parameters the {@code Map<String,?>} containing query parameters of name-value pairs
   * @return MapQueryExpression
   */
  public static MapQueryExpression using(final Class<?> type, final String query, final QueryMode mode, final Map<String,?> parameters) {
    return new MapQueryExpression(type, query, mode, parameters);
  }

  /**
   * Constructs my default state with {@code QueryMode.ReadOnly}.
   * @param type the {@code Class<?>} of the objects to be queried
   * @param query the String describing the query
   * @param parameters the {@code Map<String,?>} containing query parameters of name-value pairs
   */
  public MapQueryExpression(final Class<?> type, final String query, final Map<String,?> parameters) {
    super(type, query);
    this.parameters = Collections.unmodifiableMap(parameters);
  }

  /**
   * Constructs my default state.
   * @param type the {@code Class<?>} of the objects to be queried
   * @param query the String describing the query
   * @param mode the QueryMode
   * @param parameters the {@code Map<String,?>} containing query parameters of name-value pairs
   */
  public MapQueryExpression(final Class<?> type, final String query, final QueryMode mode, final Map<String,?> parameters) {
    super(type, query, mode);
    this.parameters = Collections.unmodifiableMap(parameters);
  }

  /*
   * @see io.vlingo.symbio.store.object.ObjectStore.QueryExpression#isMapQueryExpression()
   */
  @Override
  public boolean isMapQueryExpression() {
    return true;
  }

  @Override
  public String toString() {
    return "MapQueryExpression[type=" + type.getName() + " query=" + query + " mode=" + mode + " parameters=" + parameters + "]";
  }

  /**
   * Support a {@code Map<K,V>} that extends {@code HashMap<K,V>} along with
   * extension method {@code and(K, V)}.
   * @param <K> the key type
   * @param <V> the value type
   */
  public static class FluentMap<K,V> extends HashMap<K,V> {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs my default state.
     */
    public FluentMap() {
      super();
    }

    /**
     * Constructs my default state to have the {@code initialCapacity} for elements.
     * @param initialCapacity the int initial capacity for elements
     */
    public FluentMap(final int initialCapacity) {
      super(initialCapacity);
    }

    /**
     * Answer myself after putting {@code value} at {@code key}.
     * @param key the K typed key
     * @param value the V typed value
     * @return {@code FluentMap<K,V>}
     */
    public FluentMap<K,V> and(final K key, final V value) {
      put(key, value);
      return this;
    }
  }
}
