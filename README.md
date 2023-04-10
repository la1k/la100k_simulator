# LA100K simulator

The code provided in this repository is for simulating sequences for LA1K's 100 year anniversary sign "LA100K".

It consists of two subdirectories, darkness and websim.

Websim is a simulator that let's you display the sign and your programmed sequences in a web-browser, 
while darkness provides logic for programming sequences and broadcasting these to the web-simulator.

## Requirements
In order to run the code, you need to have `python`, `java` and `docker` installed. 

## Quickstart
websim:
- You can start the websim from the repository's root directory by running `./websim/docker/start.sh` from your terminal.
- Then open your preferred web-browser at http://localhost:8080/.
- Click the sign you want to work with, in this case it will most likely be `LA100K`.
- Click on the "Development" tab in the tabbed panel on the bottom of the page.
   - The panel will contain the current connection status to the sequence generation software.

darkness:
- Open a new terminal, and change directory to darkness. 
- Run the command `pip install -r requirements.txt`.
- Then run `python3 -m pgmplayer.development` to start the player that interacts with the websim.
- Follow the instructions given by the pgmplayer.

## Developing sequences

A _sequence_ is an animation for the UKA sign. Like any other animation, it consists of a series of still _frames_, where each frame specifies the color for every bulb on the sign at a particular point in time. More precisely, each bulb is controlled by three _channels_, one for each of the red, green, and blue color components of the bulb. We use the _DMX protocol_, which defines that each frame consists of 512 channels, numbered from 1 through 512, and each channel must have a value between 0 and 255 (inclusive). An UKA sign usually has around 100 bulbs, meaning that we'll use around 300 of the channels. A channel does not have an intrinsic color - it only carries an electric voltage - but when we connect it to one of the three colored LEDs that make up a bulb, the channel effectively "has" that color, so we may speak of "a red channel" etc. In each frame, each channel that is connected to a bulb will be set to a value that corresponds to the desired brightness of the channel's color (0 being completely off, and 255 being maximum brightness), while the unused channels will be set to 0.

When a sequence is played on the billboard, we show 20 frames per second, so a sequence that lasts for ten seconds would have more than one hundred thousand channel values across all the frames! Luckily, we have a _generator_ program that we can use to describe what the animations should look like. It also frees us from having to think about individual channels, and lets us instead think about bulbs, groups of bulbs, and what kinds of effects we want to apply to them.

### Your first sequence

Begin by selecting a name for your sequence - we will use "Rainbow" in this example. Create the file `Rainbow.kt` in the directory `generator/src/kotlin/darkness/generator/scripts/la100k` with this code in it:

```kotlin
package darkness.generator.scripts.uka21

class Rainbow : BaseScript() {
    override suspend fun run() {
        super.run()

    }
}
```

Your code will go inside the `run()` function, after the `super.run()` call. The basic idea is that you should describe how to "paint" a frame, then move on to the next frame and describe how to paint that one, and so on. The simplest way to "paint" is to use the `set()` function, which takes three parameters: the target _bulb set_ (which might be either a single bulb or a group of bulbs), and then three channel values between 0 and 255 that specify the red, green, and blue color values. In order to make it less likely that someone looking over your shoulder will see the UKA name and to help us get used to not saying the letters out loud, the first letter is called `A` in the code, the second letter is called `B`, and so on. So if you want to make the first letter bright red, you can simply do:

```kotlin
set(A, 255, 0, 0)
```

On the following lines, you can add more instructions; they will all affect the initial frame. Adding these two lines will turn the second letter bright orange and the third letter bright yellow:

```kotlin
set(B, 255, 127, 0)
set(C, 255, 255, 0)
```

When you are done "painting" your frame, add a line that simply says:

```kotlin
next()
```

This tells the generator that this frame is done and that you want to move on to the next one. Everything that follows `next()` will happen in a separate frame, which will start out having the same channel values as the previous frame. In other words, a bulb that has been set to a color will keep that color until it is told to have a different color. If you now add e.g. `set(D, 0, 255, 0)` in order to set the fourth letter to bright green in the second frame, the resulting sequence would turn on the fourth letter 0.05 seconds after the first three, since we show 20 frames per second. If you want a longer delay before that happens (and you don't need to do anything else in the meantime), you can call `skip(n)`, where `n` is the number of frames you want to skip without adding new instructions. `skip(1)` is equivalent to `next()` (a wait of 0.05 seconds), and `skip(20)` will wait for one second.

When there are no more instructions, the final frame is taken to be the last one in the sequence, which means that the sequence will end 0.05 seconds after the last frame started - so you will probably want a `skip()` call at the end unless the sequence ended with a slow fadeout. So your sequence so far might look like this:

```kotlin
set(A, 255, 0, 0)
set(B, 255, 127, 0)
set(C, 255, 255, 0)
skip(20)
set(D, 0, 255, 0)
skip(20)
```

As an exercise to the reader, you are invited to complete the sequence with all the colors of the rainbow.


### Bulbs and loops


You can target individual bulbs by using `allBulbs[0]` for the first bulb, `allBulbs[1]` for the second bulb, and so on. `allBulbs.size` tells you the number of bulbs, meaning that `allBulbs[allBulbs.size - 1]` is the last bulb. If you want to go through all the bulbs, you can use a `for` loop. In this example, we set all the bulbs in turn to red with 0.1 seconds in-between:

```kotlin
for (bulb in allBulbs) {
    set(X, 255, 0, 0)
    skip(2)
}
```

Sometimes, you'll want to count how many bulbs you've seen so far, in order to e.g. make them gradually brighter. You can then use a different type of `for` loop in which you instead get a _counter_ through the bulbs. Here, we also illustrate the use of variables (declared with `val`) to split up a computation:

```kotlin
for (i in 0 until allBulbs.size) {
    val bulb = allBulbs[i]
    val red = 2 * i
    set(bulb, red, 0, 0)
}
```

You can also loop over all the bulbs in a letter `X`. In this example, we set all of them in turn to red with 0.1 seconds in-between:

```kotlin
for (bulb in X) {
    set(X, 255, 0, 0)
    skip(2)
}
```

### More functions

There are a few more ways to set a bulb's color. In each of these functions, the target can be either a single bulb (`Bulb`) or a bulb group (`BulbGroup`) - both of these classes implement the interface `BulbSet`.

- `setHSB()` - Like `set()`, but the color is specified in the HSB color space instead. This color space is often more suitable for us than RGB, since it lets us think in terms of color hue and brightness instead of figuring out how to mix the primary colors.
- `setCoerced()` - Like `set()`, but even if the channel values are less than 0 or greater than 255, it will work. Values that are out of range will be _coerced_ to 0 if they are negative or 255 if they are greater than 255. This is convenient if you have some mathematical formula that you want to use to compute the color values but it sometimes gives values that are out of range.
- `hold()` - As mentioned, a bulb will keep the color that was most recently set on it until it is told otherwise. Sometimes, when you are dealing with effects that overlap each other (see below), you might want to use `hold()` to make the main sequence "re-set" the color of the bulb in every frame. You specify the bulb(s), the color, and for how long you want to hold it.
- `holdHSB()` - Like `hold()`, but with the color specified in HSB.

See `generator/src/kotlin/darkness/generator/api/ScriptBase.kt` for the complete list of functions that you can call.


### Effects

We often want to do things like gradually fading a bulb from one color to another, which can't be done within a single frame - instead, it's an animation that must take place across many frames. These can be packaged up in reusable _effects_. An effect is basically a sequence of its own, except that it is usually not intended to control the entire sign - usually, it can be configured to target an individual bulb or a bulb group, and it can usually be configured with other parameters like color and duration. Two of them are so commonly used that you can start them by calling a function:

- `rgbFade()` - Fades the given bulb(s) to a specified target color, in the course of the given number of frames. The fading happens by linearly fading the red, green, and blue channels independently of each other. This usually only works well for fading between brightnesses of the same color or colors that are "close" to each other.
- `hsbFade()` - Like `rgbFade()`, but instead fades the hue, saturation, and brightness components independently of each other. This will look much better if you are trying to fade between different colors, in which case it will follow the rainbow spectrum.

Most of the time, you will probably want to set a specific start color, but if you're trying to latch onto the end of some effect, it might be easier to just accept whichever value is currently there. `rgbFade()` and `hsbFade()` allow you to do either - if you don't specify a start color, it will use the current color from the previous frame. You can then easily "chain" separate fades together if an individual one doesn't give the desired result.

In the directory `generator/src/kotlin/darkness/generator/api/effects`, you will find some predefined effect classes. Feel free to add new effects. In the main sequence, you can use the `effect()` function to start one of these effects.

If you are running multiple effects or sub-sequences concurrently, they might sometimes "collide" if both try to set the same bulb in the same frame to a different color. Each effect has a `priority` property that you should set in order to decide which effects should be able to "overwrite" others - higher is better. Whenever two or more effects try to set the same bulb in the same frame, the one with the highest priority wins. If two effects that have the same priority try to set the same bulb in the same frame, the one that was started first wins (and since the main script is always registered first, it will win over any effects with the same priority). Note that this only applies in frames and on bulbs where there are actual collisions - a losing effect will still control the other bulbs it tries to set in the same frame (unless it collides with higher-priority effects there too), and in the next frame, the lower-priority effect will regain control over the bulb it "lost" in the last frame if it wants to set it there.

If you have a more involved animation that isn't general-purpose but that you want to reuse as a part of a main sequence, you can define it as a _sub-sequence_ instead of an effect, and then start it with `merge()`. (Behind the scenes, effects and sub-sequences are handled the same way, so it's more of a semantic distinction: effects should be reusable by many different scripts and have little meaning on their own, while sub-sequences are somewhat meaningful on their own and are usually intended to be a component of a specific main sequence.)

### Miscellaneous

In `generator/src/kotlin/darkness/generator/scripts/la100k/BaseScript.kt` (not to be confused with `ScriptBase`, which is the top-level base class for scripts and effects, while `BaseScript` is a subclass of `ScriptBase` that is specific to a particular UKA year - the bulb groups for that UKA sign should be defined in `BaseScript`, and the sequenes for that sign should extend `BaseScript`), you can define and functions that you want to reuse across scripts for this UKA year (if the function is specific to this year's UKA sign - if it's a completely general function, it should go in `ScriptBase` instead).

If you want to understand Kotlin more thoroughly and you have a Python background (from having taken ITGK at NTNU), check out https://khan.github.io/kotlin-for-python-developers/.
