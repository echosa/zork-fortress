(ns zork-fortress.commands.chop
  (:require [zork-fortress.game :as g]
            [zork-fortress.area :as a]
            [zork-fortress.inventory :as inv]))

(def no-trees-msg
  "There are no trees to chop.")

(def invalid-trees-msg
  "That tree doesn't exist.")

(defn logs-received-msg
  "Create a string showing the count and type of logs chopped."
  [log-count log-type]
  (str "Received " log-count " " log-type
       (if (= 1 log-count) " log." " logs.")))

(defn chop-cmd
  "The chop command."
  [game args]
  (let [tree-type (first args)
        area (g/get-current-area game)]
    (if (nil? tree-type)
      invalid-trees-msg
      (if (nil? (:trees area))
        no-trees-msg
        (let [tree (a/get-next-tree-of-type game area tree-type)]
          (if tree
            (let [log-count (:log-count tree)]
              (if (or (nil? log-count) (= 0 log-count))
                invalid-trees-msg
                (logs-received-msg log-count tree-type)))
            invalid-trees-msg))))))

(defn chop-cmd-effects
  "The chop command changes to the game."
  [game args]
  (if (empty? args)
    game
    (or
     (let [tree-type (first args)
           area (g/get-current-area game)]
       (when (some? tree-type)
         (when (some? (:trees area))
           (let [tree (a/get-next-tree-of-type game area tree-type)]
             (when tree
               (inv/add-logs-to-inventory (a/remove-tree-from-area game area tree) tree))))))
     game)))
