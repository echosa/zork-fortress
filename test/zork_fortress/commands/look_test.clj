(ns zork-fortress.commands.look-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.game :as g]
            [zork-fortress.area :as a])
  (:use [zork-fortress.commands :only [run-command]]))

(deftest test-looking-should-print-area-name
  (testing "Look command should print the area name as part of its response."
    (let [game (h/get-test-game)]
      (is (.contains (:response (:last-turn (run-command game {:trigger 'look})))
                     "First Area")))))

(deftest test-looking-should-print-area-type
  (testing "Look command should print the area type as part of its response."
    (let [game (h/get-test-game)]
      (is (.contains (:response (:last-turn (run-command game {:trigger 'look})))
                     "plains")))))

(deftest test-looking-should-print-tree-info
  (testing "Look command should print tree information as part of its response."
    (let [game (h/get-test-game)]
      (is (.contains (:response (:last-turn (run-command game {:trigger 'look})))
                     "You see 1 oak tree.")))))

(deftest test-looking-should-print-multiple-tree-info
  (testing "Look command should print tree information as part of its response."
    (let [game (h/get-test-game)
          game (a/add-tree-to-area game 1 "oak" 5)
          game (a/add-tree-to-area game 1 "pine" 5)
          response (:response (:last-turn (run-command game {:trigger 'look})))]
      (is (.contains response "You see 2 oak trees."))
      (is (.contains response "You see 1 pine tree.")))))

(deftest test-zero-tree-count-should-not-display
  (testing "Look command should not display tree counts of 0."
    (let [game (h/get-test-game)
          game (a/remove-tree-from-area game (g/get-current-area game) {:id 1 :type "oak" :log-count 10})]
      (is (not (.contains (:response (:last-turn (run-command game {:trigger 'look})))
                          "You see 0 oak trees."))))))

