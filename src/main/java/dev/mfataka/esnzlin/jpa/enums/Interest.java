package dev.mfataka.esnzlin.jpa.enums;

import java.util.List;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 28.08.2024 23:29
 */
@Getter
@RequiredArgsConstructor
public enum Interest {
    SPORTS("⚽️"),
    MUSIC("🎵"),
    TRAVEL("✈️"),
    FOOD("🍔"),
    TECHNOLOGY("💻"),
    MOVIES("🎬"),
    BOOKS("📚"),
    FITNESS("🏋️‍♂️"),
    ART("🎨"),
    GAMING("🎮"),
    NATURE("🌲"),
    ANIMALS("🐶"),
    SCIENCE("🔬"),
    FASHION("👗"),
    POLITICS("🏛️"),
    HISTORY("🏺"),
    CARS("🚗"),
    DIY("🔧"),
    EDUCATION("🎓"),
    FINANCE("💵"),
    RELIGION("⛪"),
    MEDITATION("🧘"),
    SOCIAL_MEDIA("📱"),
    SPACE("🚀"),
    HEALTH("🩺"),
    ENVIRONMENT("🌍"),
    ADVENTURE("🧗"),
    THEATER("🎭"),
    ANIME("🎌"),
    COOKING("🍳"),
    WINE("🍷"),
    BEER("🍺"),
    PODCASTS("🎙️"),
    FISHING("🎣"),
    PHILOSOPHY("🤔"),
    LITERATURE("🖋️"),
    COMICS("📖"),
    PETS("🐾"),
    BOARD_GAMES("🎲"),
    GARDENING("🌱"),
    CRAFTS("✂️"),
    MAGIC("🪄"),
    LUXURY("💎"),
    ARCHITECTURE("🏛️"),
    JOURNALISM("📰"),
    MARTIAL_ARTS("🥋"),
    YOGA("🧘‍♀️"),
    TRIVIA("❓"),
    GENEALOGY("👪"),
    SCUBA_DIVING("🤿"),
    SKIING("⛷️"),
    SNOWBOARDING("🏂"),
    SURFING("🏄‍♂️"),
    KNITTING("🧶"),
    WOODWORKING("🪚"),
    INVESTING("📈"),
    CODING("👨‍💻"),
    GRAPHIC_DESIGN("🎨"),
    PHOTOGRAPHY("📸"),
    INTERIOR_DESIGN("🏡"),
    URBAN_EXPLORATION("🏙️"),
    CLASSICAL_MUSIC("🎻"),
    ELECTRONIC_MUSIC("🎧"),
    FOLK_MUSIC("🪕"),
    OPERA("🎭"),
    DANCE("💃"),
    EQUESTRIAN("🐎"),
    WATCH_COLLECTING("⌚"),
    HIKING("🥾"),
    CAMPING("🏕️"),
    RUNNING("🏃‍♂️"),
    CYCLING("🚴‍♀️"),
    ASTRONOMY("🔭"),
    ROCK_CLIMBING("🧗‍♀️"),
    PARAGLIDING("🪂"),
    MOTORSPORTS("🏎️"),
    FLY_FISHING("🎣"),
    ANTIQUES("🪑"),
    RENAISSANCE_FAIRS("🤺"),
    POETRY("🖋️"),
    MYTHOLOGY("🔱"),
    ARCHAEOLOGY("⚒️"),
    VINTAGE_CARS("🚙"),
    BIRD_WATCHING("🦅"),
    SCULPTURE("🗿"),
    BALLET("🩰"),
    ORIGAMI("🦢"),
    CROSSWORD_PUZZLES("🧩"),
    SCRAPBOOKING("📒"),
    SEWING("🧵"),
    TAPESTRY("🧵"),
    FENCING("🤺"),
    BREWING("🍺"),
    SPIRITUALITY("🧘‍♂️"),
    STARGAZING("🌌"),
    FESTIVALS("🎉"),
    THEOLOGY("📜"),
    CHEESE_TASTING("🧀"),
    SAKE_TASTING("🍶"),
    WILDLIFE_PHOTOGRAPHY("🦁"),
    TEA_CEREMONY("🍵"),
    MINIATURE_PAINTING("🖌️"),
    CALLIGRAPHY("✍️"),
    PHILATELY("📬"),
    NUMISMATICS("🪙"),
    BOTANY("🌿"),
    HERBALISM("🌾"),
    ENTOMOLOGY("🐞"),
    GEOLOGY("🪨"),
    METEOROLOGY("🌦️"),
    TATTOOS("🦋"),
    COSPLAY("👩‍🎤"),
    MANGA("📓"),
    K_POP("🎤"),
    MEMES("😂"),
    ROLLER_SKATING("🛼"),
    MAGIC_THE_GATHERING("🧙‍♂️"),
    ESCAPE_ROOMS("🚪"),
    ESPORTS("🎮"),
    STREAMING("📺");

    private final String icon;


    public static List<InterestDTO> getAllInterests() {
        return Stream.of(Interest.values())
                .map(interest -> new InterestDTO(interest.name().replace("_", " "), interest.getIcon()))
                .toList();
    }

    public record InterestDTO(String name, String icon) {
    }
}
