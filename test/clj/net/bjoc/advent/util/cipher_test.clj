(ns net.bjoc.advent.util.cipher-test
  (:use [clojure.test :only [is testing deftest]]
        [net.bjoc.advent.util.cipher]))

(deftest rot-n-sanity
  (testing "Simplest case"
    (is (= \b (rot-n 1 \a)))
    (is (= \c (rot-n 2 \a))))
  (testing "Wraparound"
    (is (= \a (rot-n 1 \z)))
    (is (= \d (rot-n 1 \f {:charset "def"}))))
  (testing "Non-positive rotation"
    (is (= \a (rot-n 0 \a)))
    (is (= \a (rot-n -1 \b)))
    (is (= \z (rot-n -1 \a))))
  (testing "Uses modulo shortcutting"
    (is (= \q (rot-n 10000000000000000 \a))))
  (testing "Non-membership throws error"
    (is (thrown? Exception (rot-n 1 \1)))
    (is (thrown? Exception (rot-n 1 \d {:charset "abc"})))))
