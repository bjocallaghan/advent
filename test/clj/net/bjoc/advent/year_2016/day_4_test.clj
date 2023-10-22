(ns net.bjoc.advent.year-2016.day-4-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2016.day-4 :refer :all]))

(deftest checksums-by-example
  (is (= "abxyz" (name->checksum "aaaaa-bbb-z-y-x")))
  (is (= "abcde" (name->checksum "a-b-c-d-e-f-g-h")))
  (is (= "oarel" (name->checksum "not-a-real-room")))
  (is (not= "decoy" (name->checksum "totally-real-room"))))

(deftest room-sum-by-example
  (is (= 1514 (room-sum "data/year_2016/day_4_example1.input"))))

(deftest decipher-name-by-example
  (is (= "very encrypted name" (decipher-name {:name "qzmt-zixmtkozy-ivhz" :sector 343}))))
