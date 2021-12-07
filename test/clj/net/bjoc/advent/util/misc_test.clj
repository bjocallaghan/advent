(ns net.bjoc.advent.util.misc-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.util.misc]))

(deftest zip-by-examples
  (testing "Normal zip"
    (is (= [[1 4] [2 5] [3 6]] (zip [1 2 3] [4 5 6]))))
  (testing "Exotic 4 source zip"
    (is (= [[1 4 7 10] [2 5 8 11] [3 6 9 12]] (zip [1 2 3] [4 5 6] [7 8 9] [10 11 12]))))
  (testing "Uneven sources"
    (is (= [[1 4] [2 5] [3 6]] (zip [1 2 3] [4 5 6 7 8 9])))))

(deftest null-writer-sanity
  (testing "Nothing should happen when null-writer is written to"
    (is (= nil (.write null-writer "asdf")))))

(deftest find-first-sanity
  (is (= 4 (find-first even? [1 3 5 4 5 2 6])))
  (is (nil? (find-first even? [1 3 5]))))

(deftest first-sanity
  (is (= 4 (single [4])))
  (is (thrown? Exception (single [])))
  (is (thrown? Exception (single [4 5 6]))))
