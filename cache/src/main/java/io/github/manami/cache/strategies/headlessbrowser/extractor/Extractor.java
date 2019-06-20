package io.github.manami.cache.strategies.headlessbrowser.extractor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to mark {@link AnimeExtractor}s for injection.
 * TYPE: For declaration
 * PARAMETER: For constructor injection
 * FIELD: For field injection
 */
@Target(value = { ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD })
public @interface Extractor {
}
