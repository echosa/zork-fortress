# Zork Fortress 

Zork Fortress (working title, like "Blue Harvest", though I've been told it should be considered as the final name as well) is a game I'm writing inspired by Minecraft/Terraria/Dwarf Fortress with an interface similar to the text adventure games of old like Zork. The idea came from a casual conversation I had with coworkers about what other types of user interfaces such a game could could take on (Miecraft is 3D first person, Terraria is 2D side scroller, etc). I joked that an interface like Zork could be used. 

Then I thought "what if?"

Thus, Zork Fortress was born. A pet project with little direction, a vague goal, and a likely terrible idea.

But, hey, you know, it's at least a reason for me to write some test-driven and [type-driven](http://typedclojure.org) [Clojure](http://clojure.org), and I'm cool with that.

## Trying it out

The game is still very much in its infancy. There's really not much to it, but what little there is does work. If you want to try it out, simply clone the repo and run `lein run`. You'll need [Leiningen](http://leiningen.org) installed, of course.

## Dev notes

To run all the checks and tests, simply run `make` from the project's root directory. This will run `lein check` and `lein test`. If you want coverage reports for the tests and type checking, run `make coverage` instead.

The code itself may not be the best or most concise at times, but often I've gone for readability over cleverness. Also, I'm still fairly new to Clojure itself, so there's likely things I can do better or refactor to be cleaner. I am open to any and all suggestions in the form of polite and informative pull requests, which I reserve the right to completely ignore. Please so submit them, though, as this is very much a learning excersize for me and not just an entertaining hobby.
