(ns zork-fortress.types
  (:require [clojure.core.typed :as t]))

(t/defalias Player
  "The player."
  (t/HMap :mandatory {:name String}))

(t/defalias Command
  "A parsed command."
  (t/HMap :mandatory {:command t/Symbol}
          :optional {:args (t/Vec String)}))

(t/defalias Turn
  "A single player turn including command and response."
  (t/HMap :mandatory {:command t/Symbol
                      :response String}
          :optional {:invalid t/Bool}))

(t/defalias Room
  "A room in an area of the world."
  (t/HMap :mandatory {:name String}))

(t/defalias Biome
  "This is just an alias for String, to hold a biome name."
  String)

(t/defalias Tree
  "This is a Tree structure holding type and log count."
  (t/HMap :mandatory {:type String
                      :log-count t/Num}))

(t/defalias Area
  "An area of the world."
  (t/HMap :mandatory {:name String
                      :type Biome}
          :optional {:rooms (t/Vec (t/Option Room))
                     :trees (t/Vec Tree)}))

(t/defalias World
  "The world."
  (t/HMap :mandatory {:areas (t/Vec Area)}))

(t/defalias Game
  "The game."
  (t/HMap :mandatory {:player Player
                      :world World
                      :turn-history (t/Vec (t/Option Turn))}
          :optional {:last-turn Turn}))

(t/ann clojure.walk/walk [[t/Any -> t/Any] [t/Any -> t/Any] t/Any -> t/Any])
