(ns zork-fortress.ui-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.ui :as ui]))

(deftest test-default-prompt
  (testing "Default prompt should be '>'."
    (is (= "Player | > " (ui/get-user-prompt (h/get-test-game))))))
