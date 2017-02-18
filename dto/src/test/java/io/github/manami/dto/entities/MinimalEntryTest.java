package io.github.manami.dto.entities;

import io.github.manami.dto.AnimeType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class MinimalEntryTest {

    @Test(groups = "unitTest")
    public void testWithNull() {
        // given

        // when
        final boolean result = MinimalEntry.isValidMinimalEntry(null);

        // then
        assertEquals(result, false);
    }


    @Test(groups = "unitTest")
    public void testFilterEntryIsValid() {
        // given
        final FilterEntry entry = new FilterEntry("Death Note", "https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg", new InfoLink("http://myanimelist.net/anime/1535"));

        // when
        final boolean result = MinimalEntry.isValidMinimalEntry(entry);

        // then
        assertEquals(result, true);
    }


    @Test(groups = "unitTest")
    public void testFilterEntryNotValidMissingTitle() {
        // given
        final FilterEntry entry = new FilterEntry(null, "https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg", new InfoLink("http://myanimelist.net/anime/1535"));

        // when
        final boolean result = MinimalEntry.isValidMinimalEntry(entry);

        // then
        assertEquals(result, false);
    }


    @Test(groups = "unitTest")
    public void testFilterEntryIsValidMissingThumbnail() {
        // given
        final FilterEntry entry = new FilterEntry("Death Note", null, new InfoLink("http://myanimelist.net/anime/1535"));

        // when
        final boolean result = MinimalEntry.isValidMinimalEntry(entry);

        // then
        assertEquals(result, true);
    }


    @Test(groups = "unitTest")
    public void testFilterEntryNotValidMissingInfoLink() {
        // given
        final FilterEntry entry = new FilterEntry("Death Note", "https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg", null);

        // when
        final boolean result = MinimalEntry.isValidMinimalEntry(entry);

        // then
        assertEquals(result, false);
    }


    @Test(groups = "unitTest")
    public void testWatchListEntryIsValid() {
        // given
        final WatchListEntry entry = new WatchListEntry("Death Note", "https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg", new InfoLink("http://myanimelist.net/anime/1535"));

        // when
        final boolean result = MinimalEntry.isValidMinimalEntry(entry);

        // then
        assertEquals(result, true);
    }


    @Test(groups = "unitTest")
    public void testWatchListEntryNotValidMissingTitle() {
        // given
        final WatchListEntry entry = new WatchListEntry(null, "https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg", new InfoLink("http://myanimelist.net/anime/1535"));

        // when
        final boolean result = MinimalEntry.isValidMinimalEntry(entry);

        // then
        assertEquals(result, false);
    }


    @Test(groups = "unitTest")
    public void testWatchListEntryIsValidMissingThumbnail() {
        // given
        final WatchListEntry entry = new WatchListEntry("Death Note", null, new InfoLink("http://myanimelist.net/anime/1535"));

        // when
        final boolean result = MinimalEntry.isValidMinimalEntry(entry);

        // then
        assertEquals(result, true);
    }


    @Test(groups = "unitTest")
    public void testWatchListEntryNotValidMissingInfoLink() {
        // given
        final WatchListEntry entry = new WatchListEntry("Death Note", "https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg", null);

        // when
        final boolean result = MinimalEntry.isValidMinimalEntry(entry);

        // then
        assertEquals(result, false);
    }


    @Test(groups = "unitTest")
    public void testAnimeIsValid() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink(new InfoLink("http://myanimelist.net/anime/1535"));
        entry.setLocation("/death_note");
        entry.setPicture("https://myanimelist.cdn-dena.com/images/anime/9/9453.jpg");
        entry.setThumbnail("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg");
        entry.setTitle("Death Note");
        entry.setType(AnimeType.TV);

        // when
        final boolean result = MinimalEntry.isValidMinimalEntry(entry);

        // then
        assertEquals(result, true);
    }


    @Test(groups = "unitTest")
    public void testAnimeNotValidMissingTitle() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink(new InfoLink("http://myanimelist.net/anime/1535"));
        entry.setLocation("/death_note");
        entry.setPicture("https://myanimelist.cdn-dena.com/images/anime/9/9453.jpg");
        entry.setThumbnail("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg");
        entry.setType(AnimeType.TV);

        // when
        final boolean result = MinimalEntry.isValidMinimalEntry(entry);

        // then
        assertEquals(result, false);
    }


    @Test(groups = "unitTest")
    public void testAnimeNotValidMissingThumbnail() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setInfoLink(new InfoLink("http://myanimelist.net/anime/1535"));
        entry.setLocation("/death_note");
        entry.setPicture("https://myanimelist.cdn-dena.com/images/anime/9/9453.jpg");
        entry.setType(AnimeType.TV);

        // when
        final boolean result = MinimalEntry.isValidMinimalEntry(entry);

        // then
        assertEquals(result, false);
    }


    @Test(groups = "unitTest")
    public void testAnimeNotValidMissingInfoLink() {
        // given
        final Anime entry = new Anime();
        entry.setEpisodes(37);
        entry.setLocation("/death_note");
        entry.setPicture("https://myanimelist.cdn-dena.com/images/anime/9/9453.jpg");
        entry.setThumbnail("https://myanimelist.cdn-dena.com/images/anime/9/9453t.jpg");
        entry.setType(AnimeType.TV);

        // when
        final boolean result = MinimalEntry.isValidMinimalEntry(entry);

        // then
        assertEquals(result, false);
    }
}