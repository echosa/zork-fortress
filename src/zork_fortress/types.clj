(ns zork-fortress.types
  (:require [clojure.core.typed :as t]))

(t/defalias InventoryLogTypeMap (t/HMap :mandatory {:type String
                                                    :count t/Int}))

(t/defalias InventoryLogs (t/Vec InventoryLogTypeMap))

(t/defalias PlayerInventory
  "The player's inventory."
  (t/HMap :optional {:logs InventoryLogs}))

(t/defalias Player
  "The player."
  (t/HMap :mandatory {:name String
                      :inventory PlayerInventory}))

(t/defalias Command
  "A parsed command."
  (t/HMap :mandatory {:trigger t/Symbol
                      :args (t/Vec String)}))

(t/defalias Turn
  "A single player turn including command and response."
  (t/HMap :mandatory {:command Command
                      :response String}
          :optional {:invalid t/Bool}))

(t/defalias Room
  "A room in an area of the world."
  (t/HMap :mandatory {:name String}))

(t/defalias Tree
  "This is a Tree structure holding type and log count."
  (t/HMap :mandatory {:id t/Int
                      :type String
                      :log-count t/Int}))

(t/defalias TreeColl
  "This is a collection of Trees."
  (t/Vec Tree))

(t/defalias TreeTypeGroup
  "This is a collection of all Trees of a given type."
  (t/HMap :mandatory {:type String
                      :trees TreeColl}))
  
(t/defalias Area
  "An area of the world."
  (t/HMap :mandatory {:id t/Int
                      :name String
                      :type String}
          :optional {:rooms (t/Vec (t/Option Room))
                     :trees (t/Vec TreeTypeGroup)}))

(t/defalias World
  "The world."
  (t/HMap :mandatory {:areas (t/Vec Area)}))

(t/defalias TurnHistory
  "A history of turns the player has taken."
  (t/Vec (t/Option Turn)))

(t/defalias Game
  "The game."
  (t/HMap :mandatory {:player Player
                      :world World
                      :current-area t/Int
                      :turn-history TurnHistory}
          :optional {:last-turn Turn}))

(t/ann ^:no-check clojure.core/some? [t/Any -> t/Bool])
(t/ann ^:no-check clojure.walk/walk [[t/Any -> t/Any] [t/Any -> t/Any] t/Any -> t/Any])

(defmacro assoc-in*
  "Associates a value in a nested associative structure, where ks is a
  sequence of keys and v is the new value and returns a new nested structure.
  If any levels do not exist, hash-maps will be created."
  {:added "1.0"
   :static true}
  [m [k & ks] v]
  (if ks
    `(let [m# ~m k# ~k] (assoc m# k# (assoc-in* (get m# k#) ~ks ~v)))
    `(assoc ~m ~k ~v))) 

(defmacro update-in*
  "'Updates' a value in a nested associative structure, where ks is a
  sequence of keys and f is a function that will take the old value
  and any supplied args and return the new value, and returns a new
  nested structure. If any levels do not exist, hash-maps will be
  created."
  ([m [k & ks] f & args]
   (if ks
     `(let [m# ~m k# ~k] (assoc m# k# (update-in* (get m# k#) ~ks ~f ~@args)))
     `(let [m# ~m k# ~k] (assoc m# k# (~f (get m# k#) ~@args)))))) 
