(ns zork-fortress.types
  (:require [clojure.core.typed :as t]))

(t/defalias Player
  "The player."
  (t/HMap :mandatory {:name String}))

(t/defalias Turn
  "A single player turn including command and response."
  (t/HMap :mandatory {:command t/Symbol
                      :response String}))
  
(t/defalias Game
  "The game."
  (t/HMap :mandatory {:player Player}
          :optional {:last-turn Turn}))

