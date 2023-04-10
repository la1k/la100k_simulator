package darkness.generator.api.effects

import darkness.generator.api.ScriptBase

/**
 * This subclass doesn't add any features; it's only here for semantics:
 * a class should subclass [EffectBase] instead of [ScriptBase] if it is only
 * meant to be used as an effect in other scripts,
 * and not as a top-level script.
 */
abstract class EffectBase : ScriptBase()
