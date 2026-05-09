package br.com.ufrn.GMS.Session;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.javatuples.Pair;

public class SongsManager {

  private static final Random random = new Random();

  // TODO: se estiver performando mal, mudar pra leitura de arquivos mesmo
  private static final Map<Long, Pair<String, List<String>>> data = Map.of(
      1L, Pair.with("Telomeres", List.of(
          "You guide me in",
          "To safety and silence (Oh)",
          "As you breathe me out",
          "I drink you in (Oh)",
          "And we go beyond the farthest reaches",
          "Where the light bends and wraps beneath us",
          "And I know as you collapse into me",
          "This is the start of something",
          "Rivers and oceans",
          "We could beckon, no",
          "Your eyes and your limbs",
          "Are instruments to pick apart",
          "The distance within",
          "Let the tides carry you back to me",
          "The past, the future",
          "Through death",
          "My arms are open",
          "We go beyond the farthest reaches",
          "Where the light bends and wraps beneath us",
          "And I know as you collapse into me",
          "This is the start of something new")),
      2L, Pair.with("Caramel", List.of(
          "Count me out like sovereigns, payback for the good times",
          "Right foot in the roses, left foot on a landmine",
          "I'm not gonna be there tripping on the grapevine",
          "They can sing the words while I cry into the bassline",
          "Wear me out like Prada, devil in my detail",
          "I swear it's getting harder even just to exhale",
          "Backed up into corners, bitter in the lens",
          "I'm sick of tryna hide it every time thеy take mine",
          "So stick to me",
          "Stick to mе like caramel",
          "Walk beside me till you feel nothing as well",
          "They ask me, \"Is it going good in the garden?\"",
          "Say, \"I'm lost, but I beg no pardon\"",
          "Up on the dice, but low on the cards",
          "I try not to talk about how it's harder now",
          "Can I get a mirror side-stage?",
          "Looking sideways at my own visage, gettin' worse",
          "Every time they try to shout my real name just to get a rise from me",
          "Acting like I'm never stressed out by the hearsay",
          "I guess that's what I get for tryna hide in the limelight",
          "Guess that's what I get for having 20/20 hindsight",
          "Everybody wants eyes on 'em, I just wanna hear you sing that top line",
          "And if you don't think I mean it, then I understand",
          "But I'm still glad you came, so let me see those hands",
          "So stick to me",
          "Stick to me like caramel",
          "Walk beside me till you feel nothin' as well",
          "I'm fallin' free of the final parallel",
          "The sweetest dreams are bitter",
          "But there's no one left to tell",
          "Too young to get bitter over it all",
          "Too old to retaliate like before",
          "Too blessed to be caught ungrateful, I know",
          "So I'll keep dancin' along to the rhythm",
          "This stage is a prison (Too young to get bitter over it all), a beautiful nightmare",
          "A war of attrition (Too old to retaliate like before), I'll take what I'm given",
          "The deepest incisions (Too blessed to be caught ungrateful, I know), I thought I got better",
          "But maybe I didn't",
          "(In these days of days) Tell me, did I give you what you came for?",
          "(I wish it all away) Terrified to answer my own front door",
          "(I thought things had changed) Missin' my wings in a realm of angels",
          "(But everything's the same)",
          "So I'll keep dancin' along to the rhythm",
          "This stage is a prison, a beautiful nightmare",
          "A war of attrition, I'll take what I'm given",
          "The deepest incisions, I thought I got better",
          "But maybe I didn't")));

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
