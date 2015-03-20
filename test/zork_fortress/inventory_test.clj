(ns zork-fortress.inventory-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.inventory :as inv]))

(deftest test-adding-multiple-logs-to-inventory
  (testing "Player should have both types of logs in inventory."
    (let [game (h/get-test-game)
          game (inv/add-logs-to-inventory game {:id 1 :type "pine" :log-count 5})
          game (inv/add-logs-to-inventory game {:id 1 :type "oak" :log-count 2})]
      (is (= 5 (inv/get-inventory-log-count game "pine")))
      (is (= 2 (inv/get-inventory-log-count game "oak"))))))

(deftest test-adding-multiple-of-the-same-log-type-to-inventory
  (testing "Player should have the correct number of logs in inventory."
    (let [game (h/get-test-game)
          game (inv/add-logs-to-inventory game {:id 1 :type "oak" :log-count 5})
          game (inv/add-logs-to-inventory game {:id 2 :type "oak" :log-count 2})]
      (is (= 7 (inv/get-inventory-log-count game "oak"))))))
