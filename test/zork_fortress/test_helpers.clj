(ns zork-fortress.test-helpers)

(defn get-test-player
  "Returns a test player."
  [& {:keys [name]}]
  {:name (if (nil? name) "Player" name)
   :inventory {}})

(defn get-test-area
  "Returns a test area."
  [& {:keys [without-trees]}]
  (let [area {:id 1
              :name "First Area"
              :type "plains"}]
    (if without-trees
      area
      (merge area {:trees [{:type "oak" :trees [{:id 1 :type "oak" :log-count 10}]}]}))))

(defn get-test-world
  "Returns a test world."
  [& {:keys [without-trees]}]
  {:areas [(if without-trees
             (get-test-area :without-trees without-trees)
             (get-test-area))]})

(defn get-test-game
  "Returns a test game."
  [& {:keys [without-trees last-turn turn-history]}]
  (let [game {:player (get-test-player)
              :world (if without-trees
                       (get-test-world :without-trees without-trees)
                       (get-test-world))
              :current-area 1
              :turn-history []}
        game (if (nil? last-turn)
                game
                (merge game {:last-turn last-turn}))
        game (if (nil? turn-history)
                game
                (merge game {:turn-history turn-history}))]
    game))
