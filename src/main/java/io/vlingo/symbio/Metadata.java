// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.symbio;

import java.util.Comparator;
import java.util.Optional;

public class Metadata implements Comparable<Metadata> {
  public final static Object EmptyObject = new Object() { @Override public String toString() { return "(empty)"; } };

  public final Object object;
  public final String operation;
  public final String value;

  public static Metadata nullMetadata() {
    return new Metadata(EmptyObject, "", "");
  }

  public static Metadata withObject(final Object object) {
    return new Metadata(object, "", "");
  }

  public static Metadata withOperation(final String operation) {
    return new Metadata(EmptyObject, "", operation);
  }

  public static Metadata withValue(final String value) {
    return new Metadata(EmptyObject, value, "");
  }

  public static Metadata with(final String value, final String operation) {
    return new Metadata(EmptyObject, value, operation);
  }

  public static Metadata with(final Object object, final String value, final String operation) {
    return new Metadata(object, value, operation);
  }

  public static Metadata with(final Object object, final String value, final Class<?> operationType) {
    return with(object, value, operationType, true);
  }

  public static Metadata with(final Object object, final String value, final Class<?> operationType, final boolean compact) {
    final String operation = compact ? operationType.getSimpleName() : operationType.getName();
    return new Metadata(object, value, operation);
  }

  public Metadata(final Object object, final String value, final String operation) {
    if (object == null) this.object = EmptyObject; else this.object = object;

    if (value == null) this.value = ""; else this.value = value;

    if (operation == null) this.operation = ""; else this.operation = operation;
  }

  public Metadata(final String value, final String operation) {
    this(EmptyObject, value, operation);
  }

  public Metadata() {
    this(EmptyObject, "", "");
  }

  public boolean hasObject() {
    return object != EmptyObject;
  }

  public boolean hasOperation() {
    return !operation.isEmpty();
  }

  public boolean hasValue() {
    return !value.isEmpty();
  }

  public boolean isEmpty() {
    return !hasOperation() && !hasValue();
  }

  public Object object() {
    return object;
  }

  public Optional<Object> optionalObject() {
    return hasObject() ? Optional.of(object) : Optional.empty();
  }

  public String operation() {
    return operation;
  }

  public String value() {
    return value;
  }

  @SuppressWarnings("unchecked")
  public <T> T typedObject() {
    return (T) object;
  }

  @Override
  public int compareTo(final Metadata other) {
    if (!this.object.equals(other.object)) return 1;
    return Comparator
            .comparing((Metadata m) -> m.value)
            .thenComparing(m -> m.operation)
            .compare(this, other);
  }

  @Override
  public int hashCode() {
    return 31 * value.hashCode() + operation.hashCode() + object.hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || other.getClass() != this.getClass()) {
      return false;
    }

    final Metadata otherMetadata = (Metadata) other;

    return value.equals(otherMetadata.value) &&
            operation.equals(otherMetadata.operation) &&
            object.equals(otherMetadata.object);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() +
            "[value=" + value + " operation=" + operation + " object=" + object + "]";
  }
}
