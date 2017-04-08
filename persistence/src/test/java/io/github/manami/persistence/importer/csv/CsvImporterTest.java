package io.github.manami.persistence.importer.csv;

import static io.github.manami.dto.TestConst.UNIT_TEST_GROUP;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.google.common.eventbus.EventBus;

import io.github.manami.dto.AnimeType;
import io.github.manami.dto.entities.Anime;
import io.github.manami.dto.entities.FilterEntry;
import io.github.manami.dto.entities.WatchListEntry;
import io.github.manami.persistence.PersistenceFacade;
import io.github.manami.persistence.inmemory.InMemoryPersistenceHandler;
import io.github.manami.persistence.inmemory.animelist.InMemoryAnimeListHandler;
import io.github.manami.persistence.inmemory.filterlist.InMemoryFilterListHandler;
import io.github.manami.persistence.inmemory.watchlist.InMemoryWatchListHandler;

public class CsvImporterTest {

    private static final String TEST_ANIME_LIST_FILE = "test_anime_list.csv";
    private CsvImporter csvImporter;
    private Path file;
    private PersistenceFacade persistenceFacade;


    @BeforeMethod
    public void setUp() throws IOException {
        final InMemoryPersistenceHandler inMemoryPersistenceHandler = new InMemoryPersistenceHandler(new InMemoryAnimeListHandler(), new InMemoryFilterListHandler(), new InMemoryWatchListHandler());
        persistenceFacade = new PersistenceFacade(inMemoryPersistenceHandler, mock(EventBus.class));
        csvImporter = new CsvImporter(persistenceFacade);
        final ClassPathResource resource = new ClassPathResource(TEST_ANIME_LIST_FILE);
        file = resource.getFile().toPath();
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void testThatAnimeListIsParsedCorrectly() throws SAXException, ParserConfigurationException, IOException {
        // given

        // when
        csvImporter.importFile(file);

        // then
        final List<Anime> fetchAnimeList = persistenceFacade.fetchAnimeList();
        assertNotNull(fetchAnimeList);
        assertEquals(fetchAnimeList.isEmpty(), false);
        assertEquals(fetchAnimeList.size(), 2);
        final Anime bokuDake = fetchAnimeList.get(0);
        final Anime rurouniKenshin = fetchAnimeList.get(1);
        assertNotNull(bokuDake);
        assertEquals(bokuDake.getEpisodes(), 12);
        assertEquals(bokuDake.getInfoLink().getUrl(), "http://myanimelist.net/anime/31043");
        assertEquals(bokuDake.getLocation(), "/anime/series/boku_dake_ga_inai_machi");
        assertEquals(bokuDake.getTitle(), "Boku dake ga Inai Machi");
        assertEquals(bokuDake.getType(), AnimeType.TV);
        assertNotNull(rurouniKenshin);
        assertEquals(rurouniKenshin.getEpisodes(), 4);
        assertEquals(rurouniKenshin.getInfoLink().getUrl(), "http://myanimelist.net/anime/44");
        assertEquals(rurouniKenshin.getLocation(), "/anime/series/rurouni_kenshin");
        assertEquals(rurouniKenshin.getTitle(), "Rurouni Kenshin: Meiji Kenkaku Romantan - Tsuiokuhen");
        assertEquals(rurouniKenshin.getType(), AnimeType.OVA);
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void testThatWatchListIsParsedCorrectly() throws SAXException, ParserConfigurationException, IOException {
        // given

        // when
        csvImporter.importFile(file);

        // then
        final List<WatchListEntry> fetchWatchList = persistenceFacade.fetchWatchList();
        assertNotNull(fetchWatchList);
        assertEquals(fetchWatchList.isEmpty(), false);
        assertEquals(fetchWatchList.size(), 1);
        final WatchListEntry deathNoteRewrite = fetchWatchList.get(0);
        assertNotNull(deathNoteRewrite);
        assertEquals(deathNoteRewrite.getInfoLink().getUrl(), "http://myanimelist.net/anime/2994");
        assertEquals(deathNoteRewrite.getThumbnail(), "https://myanimelist.cdn-dena.com/images/qm_50.gif");
        assertEquals(deathNoteRewrite.getTitle(), "Death Note Rewrite");
    }


    @Test(groups = UNIT_TEST_GROUP)
    public void testThatFilterListIsParsedCorrectly() throws SAXException, ParserConfigurationException, IOException {
        // given

        // when
        csvImporter.importFile(file);

        // then
        final List<FilterEntry> fetchFilterList = persistenceFacade.fetchFilterList();
        assertNotNull(fetchFilterList);
        assertEquals(fetchFilterList.isEmpty(), false);
        assertEquals(fetchFilterList.size(), 1);
        final FilterEntry gintama = fetchFilterList.get(0);
        assertNotNull(gintama);
        assertEquals(gintama.getInfoLink().getUrl(), "http://myanimelist.net/anime/918");
        assertEquals(gintama.getThumbnail(), "https://myanimelist.cdn-dena.com/images/qm_50.gif");
        assertEquals(gintama.getTitle(), "Gintama");
    }
}
