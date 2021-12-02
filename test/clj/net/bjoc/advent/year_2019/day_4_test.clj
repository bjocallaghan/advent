(ns net.bjoc.advent.year-2019.day-4-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.year-2019.day-4]))

(deftest six-digits?-sanity
  (is (six-digits? 123456))
  (is (six-digits? 123456))
  (is (six-digits? 123456))
  (is (not (six-digits? 12)))
  (is (not (six-digits? 010000)))
  (is (not (six-digits? 1234567))))

(deftest has-equal-adjacents?-sanity
  (is (has-equal-adjacents? 1223))
  (is (has-equal-adjacents? 1010101101))
  (is (not (has-equal-adjacents? 123456)))
  (is (not (has-equal-adjacents? 12121212))))

(deftest increasing-digits?-sanity
  (is (increasing-digits? 12345))
  (is (increasing-digits? 12222345))
  (is (not (increasing-digits? 12222045)))
  (is (not (increasing-digits? 987654321))))

(deftest has-unique-equal-adjacents?-sanity
  (is (has-unique-equal-adjacents? 112233))
  (is (has-unique-equal-adjacents? 111122))
  (is (not (has-unique-equal-adjacents? 123334)))
  (is (not (has-unique-equal-adjacents? 111234)))
  (is (not (has-unique-equal-adjacents? 123444))))
