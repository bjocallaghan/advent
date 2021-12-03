(ns net.bjoc.advent.util.binary-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.util.binary]))

(deftest read-binary-string-sanity
  (testing "Parsing binary strings"
    (is (= 0 (read-binary-string "0")))
    (is (= 1 (read-binary-string "1")))
    (is (= 2 (read-binary-string "10")))
    (is (= 3 (read-binary-string "11")))
    (is (= 4 (read-binary-string "100")))
    (is (= 5 (read-binary-string "101")))
    (is (= 6 (read-binary-string "110")))
    (is (= 7 (read-binary-string "111")))
    (is (= 8 (read-binary-string "1000"))))
  (testing "Parsing leading zeroes"
    (is (= 1 (read-binary-string "01")))
    (is (= 1 (read-binary-string "0000001")))
    (is (= 5 (read-binary-string "0101")))
    (is (= 5 (read-binary-string "000000101"))))
  (testing "Zero"
    (is (= 0 (read-binary-string "0")))
    (is (= 0 (read-binary-string "00000"))))
  (testing "Throws error for bad format"
    (is (thrown? NumberFormatException(read-binary-string "xxx")))))
