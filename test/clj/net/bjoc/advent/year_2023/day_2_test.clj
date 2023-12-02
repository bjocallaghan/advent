(ns net.bjoc.advent.year-2023.day-2-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2023.day-2 :refer :all]))

(deftest possible?-by-example
  (is (= true (possible? (line->summary "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"))))
  (is (= true (possible? (line->summary "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue"))))
  (is (= false (possible? (line->summary "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red"))))
  (is (= false (possible? (line->summary "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red"))))
  (is (= true (possible? (line->summary "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"))))
  (is (= true (possible? (line->summary "Game 1000: 11 red; 11 red")))))

(deftest file->score-by-example
  (is (= 8 (file->score "data/year_2023/day_2_example1.input"))))

(deftest summary->power-by-example
  (is (= 48 (summary->power (line->summary "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"))))
  (is (= 12 (summary->power (line->summary "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue"))))
  (is (= 1560 (summary->power (line->summary "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red"))))
  (is (= 630 (summary->power (line->summary "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red"))))
  (is (= 36 (summary->power (line->summary "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green")))))
