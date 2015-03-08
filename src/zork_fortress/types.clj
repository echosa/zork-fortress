(ns zork-fortress.types
  (:require [clojure.core.typed :as t]))

(t/defalias PlayerInventory
  "The player's inventory."
  (t/HMap :optional {:logs (t/HMap :mandatory {:type String
                                               :count t/Int})}))

(t/defalias Player
  "The player."
  (t/HMap :mandatory {:name String
                      :inventory PlayerInventory}))

(t/defalias Command
  "A parsed command."
  (t/HMap :mandatory {:trigger t/Symbol}
          :optional {:args (t/Vec String)}))

(t/defalias Turn
  "A single player turn including command and response."
  (t/HMap :mandatory {:command Command
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
  (t/HMap :mandatory {:id t/Int
                      :type String
                      :log-count t/Int}))

(t/defalias TreeTypeColl
  "This is a collection of all Trees of a given type."
  (t/HMap :mandatory {:type String :trees (t/Vec Tree)}))
  
(t/defalias Area
  "An area of the world."
  (t/HMap :mandatory {:id t/Int
                      :name String
                      :type Biome}
          :optional {:rooms (t/Vec (t/Option Room))
                     :trees (t/Vec TreeTypeColl)}))

(t/defalias World
  "The world."
  (t/HMap :mandatory {:areas (t/Vec Area)}))

(t/defalias Game
  "The game."
  (t/HMap :mandatory {:player Player
                      :world World
                      :current-area t/Int
                      :turn-history (t/Vec (t/Option Turn))}
          :optional {:last-turn Turn}))

(t/ann ^:no-check clojure.walk/walk [[t/Any -> t/Any] [t/Any -> t/Any] t/Any -> t/Any])
(t/ann ^:no-check clojure.core/update-in [Game t/Any t/Any t/Any t/Any -> Game])
