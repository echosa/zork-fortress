(ns zork-fortress.test-helpers
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]))

(t/ann get-test-player [& :optional {:name String} -> t2/Player])
(defn get-test-player
  "Returns a test player."
  [& {:keys [name]}]
  {:pre [(or (nil? name) (string? name))]}
  {:name (if (nil? name) "Player" name)})

(t/ann get-test-world [-> t2/World])
(defn get-test-world
  "Returns a test world."
  []
  {:areas [{:name "First Area" :type "Plains"}]})

(t/ann get-test-game [-> t2/Game])
(defn get-test-game
  "Returns a test game."
  []
  {:player (get-test-player)
   :world (get-test-world)
   :last-turn {:command 'look
               :response "You see nothing."}
   :turn-history []})