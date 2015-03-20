(ns zork-fortress.ui-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.ui :as ui]))

(deftest test-default-prompt
  (testing "Default prompt should be '>'."
    (is (= "Player | > " (ui/get-user-prompt (h/get-test-game))))))

(deftest test-parsing-input-without-arguments
  (testing "Input should be parsed as command."
    (is (= {:trigger 'look :args []} (ui/parse-input "look")))))

(deftest test-parsing-input-with-arguments
  (testing "Input should be parsed into command and arguments."
    (is (= {:trigger 'history :args ["4"]}
           (ui/parse-input "history 4")))))

