(ns net.bjoc.advent.year-2023.day-1-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2023.day-1 :refer :all]))

(deftest line->num-by-example
  (is (= 12 (line->num "1abc2")))
  (is (= 38 (line->num "pqr3stu8vwx")))
  (is (= 15 (line->num "a1b2c3d4e5f")))
  (is (= 77 (line->num "treb7uchet"))))

(deftest file->sum-by-example
  (is (= 142 (file->sum "data/year_2023/day_1_example1.input"))))

(deftest line->num-2-by-example
  (is (= 29 (line->num-2 "two1nine")))
  (is (= 83 (line->num-2 "eightwothree")))
  (is (= 13 (line->num-2 "abcone2threexyz")))
  (is (= 24 (line->num-2 "xtwone3four")))
  (is (= 42 (line->num-2 "4nineeightseven2")))
  (is (= 14 (line->num-2 "zoneight234")))
  (is (= 76 (line->num-2 "7pqrstsixteen"))))

(deftest file->sum-by-example
  (is (= 281 (file->sum-2 "data/year_2023/day_1_example2.input"))))
