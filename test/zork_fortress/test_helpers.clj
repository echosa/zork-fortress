(ns zork-fortress.test-helpers
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]))

(t/ann get-test-player [& :optional {:name String} -> t2/Player])
(defn get-test-player
  "Returns a test player."
  [& {:keys [name]}]
  {:pre [(or (nil? name) (string? name))]}
  {:name (if (nil? name) "Player" name)})

(t/ann get-test-area [-> t2/Area])
(defn get-test-area
  "Returns a test area."
  []
  {:name "First Area" 
   :type "plains"
   :trees [{:type "oak" :log-count 10}]})

(t/ann get-test-world [-> t2/World])
(defn get-test-world
  "Returns a test world."
  []
  {:areas [(get-test-area)]})

(t/ann get-test-game [& :optional {:last-turn t2/Turn
                                   :turn-history (t/Vec (t/Option t2/Turn))}
                      -> t2/Game])
(defn get-test-game
  "Returns a test game."
  [& {:keys [last-turn turn-history]}]
  (let [game {:player (get-test-player)
              :world (get-test-world)
              :current-area (get-test-area)
              :turn-history []}
        game (if (nil? last-turn)
                game
                (merge game {:last-turn last-turn}))
        game (if (nil? turn-history)
                game
                (merge game {:turn-history turn-history}))]
    game))
