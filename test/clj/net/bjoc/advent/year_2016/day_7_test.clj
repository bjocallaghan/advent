(ns net.bjoc.advent.year-2016.day-7-test
  (:require [clojure.test :refer [is testing deftest]]
            [net.bjoc.advent.year-2016.day-7 :refer :all]))

(deftest primitives-by-example
  (testing "abba? predicate"
    (is (abba? "abba"))
    (is (abba? "oxxo"))
    (is (not (abba? "abcd")))
    (is (not (abba? "aaaa"))))
  (testing "net splitups"
    (let [result (line->nets "abba[mnop]qrst")]
      (is (= ["abba" "qrst"] (:supernets result)))
      (is (= ["mnop"] (:hypernets result))))))

(deftest tls?-by-example
  (is (tls? "abba[mnop]qrst"))
  (is (not (tls? "abcd[bddb]xyyx")))
  (is (not (tls? "aaaa[qwer]tyui")))
  (is (tls? "ioxxoj[asdfgh]zxcvbn")))

(deftest ssl?-by-example
  (is (ssl? "aba[bab]xyz"))
  (is (not (ssl? "xyx[xyx]xyx")))
  (is (ssl? "aaa[kek]eke"))
  (is (ssl? "zazbz[bzb]cdb")))
