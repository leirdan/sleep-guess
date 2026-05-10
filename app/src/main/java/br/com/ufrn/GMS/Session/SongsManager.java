package br.com.ufrn.GMS.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.javatuples.Pair;

import br.com.ufrn.GMS.Reverbs.GMSReverb;
import br.com.ufrn.GMS.Utils.GMSStatusCode;

public class SongsManager {

  private static final Random random = new Random();

  private static final Map<Long, Pair<String, List<String>>> data = new HashMap<>();

  public static Optional<GMSReverb> init() {
    try {
      Path songsPath = Paths.get("src/main/resources/songs");
      AtomicLong counter = new AtomicLong(1);

      Files.walk(songsPath)
          .filter(Files::isRegularFile)
          .forEach(path -> {
            try {
              List<String> lines = Files.readAllLines(path);
              String title = lines.get(0);
              List<String> lyrics = lines.subList(1, lines.size());

              data.put(
                  counter.getAndIncrement(),
                  Pair.with(title, lyrics));

            } catch (IOException e) {
              System.out.println("Couldn't capture a song..");
              // DO nothing
            }
          });
      return Optional.empty();
    } catch (IOException e) {
      return Optional.of(new GMSReverb(GMSStatusCode.INTERNAL_PANIC, "ERROR. Failed to load songs."));
    }
  }

  public static Long nextSong(Set<Long> usedSongs) {
    List<Long> ids = SongsManager.data
        .keySet()
        .stream()
        .filter(id -> !usedSongs.contains(id))
        .toList();

    if (ids.isEmpty()) {
      return -1L;
    }

    return ids.get(random.nextInt(ids.size()));
  }

  public static Pair<String, List<String>> getSong(Long id) {
    return SongsManager.data.get(id);
  }
};
