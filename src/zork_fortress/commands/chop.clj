(ns zork-fortress.commands.chop
  (:require [zork-fortress.game :as g]
            [zork-fortress.area :as a]
            [zork-fortress.inventory :as inv]))

(def no-trees-msg
  "There are no trees to chop.")

(def invalid-trees-msg
  "That tree doesn't exist.")

(def ambiguous-tree-msg
  "Which tree would you like to chop?")

(defn logs-received-msg
  "Create a string showing the count and type of logs chopped."
  [log-count log-type]
  (str "Received " log-count " " log-type
       (if (= 1 log-count) " log." " logs.")))

(defn parse-tree-type
  "Parse out the tree type to chop."
  [game type]
  (let [area (g/get-current-area game)
        tree-types (a/get-tree-types area)]
    (if (and (= "tree" type)
             (= 1 (count tree-types)))
      (first tree-types)
      type)))

(defn chop-cmd
  "The chop command."
  [game args]
  (let [tree-type (parse-tree-type game (first args))
        area (g/get-current-area game)]
    (if (nil? tree-type)
      invalid-trees-msg
      (if (= "tree" tree-type)
        ambiguous-tree-msg
        (if (nil? (:trees area))
          no-trees-msg
          (let [tree (a/get-next-tree-of-type game area tree-type)]
            (if tree
              (let [log-count (:log-count tree)]
                (if (or (nil? log-count) (= 0 log-count))
                  invalid-trees-msg
                  (logs-received-msg log-count tree-type)))
              invalid-trees-msg)))))))

(defn chop-cmd-effects
  "The chop command changes to the game."
  [game args]
  (if (empty? args)
    game
    (or
     (let [tree-type (parse-tree-type game (first args))
           area (g/get-current-area game)]
       (when (and (some? tree-type)
                  (not (= "tree" tree-type)))
         (when (some? (:trees area))
           (let [tree (a/get-next-tree-of-type game area tree-type)]
             (when tree
               (inv/add-logs-to-inventory (a/remove-tree-from-area game area tree) tree))))))
     game)))
