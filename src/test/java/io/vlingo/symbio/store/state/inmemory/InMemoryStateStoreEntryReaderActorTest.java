// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.symbio.store.state.inmemory;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.actors.testkit.TestWorld;
import io.vlingo.common.Completes;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.Source;
import io.vlingo.symbio.StateAdapterProvider;
import io.vlingo.symbio.store.state.Entity1;
import io.vlingo.symbio.store.state.Entity1.Entity1StateAdapter;
import io.vlingo.symbio.store.state.Entity2;
import io.vlingo.symbio.store.state.MockStateStoreDispatcher;
import io.vlingo.symbio.store.state.MockStateStoreResultInterest;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.StateStoreEntryReader;
import io.vlingo.symbio.store.state.StateTypeStateStoreMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class InMemoryStateStoreEntryReaderActorTest {
  private static final String Id1 = "123-A";
  private static final String Id2 = "123-B";
  private static final String Id3 = "123-C";

  private MockStateStoreDispatcher dispatcher;
  private EntryAdapterProvider entryAdapterProvider;
  private MockStateStoreResultInterest interest;
  private StateStoreEntryReader<TextEntry> reader;
  private StateStore store;
  private TestWorld testWorld;
  private World world;

  @Test
  public void testThatEntryReaderReadsOne() {
    final AccessSafely access = interest.afterCompleting(3);
    dispatcher.afterCompleting(0);

    store.write(Id1, new Entity1(Id1, 10), 1, Arrays.asList(new Event1()), interest);
    store.write(Id2, new Entity2(Id2, "20"), 1, Arrays.asList(new Event2()), interest);
    store.write(Id3, new Entity1(Id3, 30), 1, Arrays.asList(new Event3()), interest);

    assertEquals(new Event1(), access.readFrom("sources"));
    assertEquals(new Event2(), access.readFrom("sources"));
    assertEquals(new Event3(), access.readFrom("sources"));

    final TextEntry entry1 = reader.readNext().await();
    assertEquals(entryAdapterProvider.asEntry(new Event1(), Metadata.nullMetadata()).withId("0"), entry1);
    final TextEntry entry2 = reader.readNext().await();
    assertEquals(entryAdapterProvider.asEntry(new Event2(), Metadata.nullMetadata()).withId("1"), entry2);
    final TextEntry entry3 = reader.readNext().await();
    assertEquals(entryAdapterProvider.asEntry(new Event3(), Metadata.nullMetadata()).withId("2"), entry3);

    reader.rewind();
    assertEquals(Arrays.asList(entry1, entry2, entry3), reader.readNext(3).await());
  }

  @Before
  public void setUp() {
    testWorld = TestWorld.startWithDefaults("test-store");
    world = testWorld.world();

    interest = new MockStateStoreResultInterest();
    dispatcher = new MockStateStoreDispatcher(interest);

    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(world);
    entryAdapterProvider = new EntryAdapterProvider(world);

    stateAdapterProvider.registerAdapter(Entity1.class, new Entity1StateAdapter());
    // NOTE: No adapter registered for Entity2.class because it will use the default

    store = world.actorFor(StateStore.class, InMemoryStateStoreActor.class, dispatcher);

    final Completes<StateStoreEntryReader<TextEntry>> completes = store.entryReader("test");
    reader = completes.await();

    StateTypeStateStoreMap.stateTypeToStoreName(Entity1.class, Entity1.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(Entity2.class, Entity2.class.getSimpleName());
  }

  public static abstract class Event extends Source<Event> {
    @Override
    public boolean equals(final Object other) {
      if (other == null || other.getClass() != getClass()) {
        return false;
      }
      return true;
    }
  }

  public static final class Event1 extends Event { }

  public static final class Event2 extends Event { }

  public static final class Event3 extends Event { }
}