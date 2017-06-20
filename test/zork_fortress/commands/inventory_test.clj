(ns zork-fortress.commands.inventory-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.game :as g]
            [zork-fortress.area :as a]
            [zork-fortress.commands.inventory :as inv])
  (:use [zork-fortress.commands :only [run-cmd]]))

(deftest test-empty-inventory-should-print-correct-msg
  (testing "Trying to inventory trees where there are none should show an error message."
    (let [game (h/get-test-game)]
      (is (= inv/empty-inventory-msg
             (:response (:last-turn (run-cmd game {:trigger 'inventory}))))))))

(deftest test-inventory-should-print-correct-logs
  (testing "Showing inventory should show correct log count."
    (let [game (run-cmd (h/get-test-game) {:trigger 'chop :args ["oak"]})]
      (is (.contains (:response (:last-turn (run-cmd game {:trigger 'inventory})))
                     "10 oak logs")))))

(deftest test-inventory-should-print-correct-multiple-types-of-logs
  (testing "Showing inventory should show correct log count with multiple types."
    (let [game (h/get-test-game)
          game (a/add-tree-to-area game 1 "pine" 1)
          game (run-cmd game {:trigger 'chop :args ["oak"]})
          game (run-cmd game {:trigger 'chop :args ["pine"]})
          last-turn-response (:response (:last-turn (run-cmd game {:trigger 'inventory})))]
      (is (.contains last-turn-response "10 oak logs"))
      (is (.contains last-turn-response "10 oak logs"))
      (is (not (.contains last-turn-response "1 pine logs"))))))

