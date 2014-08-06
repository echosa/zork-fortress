(ns zork-fortress.cmds.look-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h])
  (:use [zork-fortress.cmds :only [run-cmd]]))

(deftest test-looking-should-print-area-name
  (testing "Look command should print the area name as part of its response."
    (let [game (h/get-test-game)]
      (is (.contains (:response (:last-turn (run-cmd game {:trigger 'look})))
          "First Area")))))

(deftest test-looking-should-print-area-type
  (testing "Look command should print the area type as part of its response."
    (let [game (h/get-test-game)]
      (is (.contains (:response (:last-turn (run-cmd game {:trigger 'look})))
          "plains")))))
