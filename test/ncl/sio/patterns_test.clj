;; The contents of this file are subject to the LGPL License, Version 3.0.

;; Copyright (C) 2013-2014, Newcastle University

;; This program is free software: you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
;; GNU General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with this program. If not, see http://www.gnu.org/licenses/.

(ns ncl.sio.patterns_test
  (:refer-clojure :exclude [atom])
  (:use [clojure.test])
  (:require [tawny.owl :as o]
            [ncl.sio.patterns :as p])
  (:import (org.semanticweb.owlapi.model
            OWLAnnotation OWLObjectProperty OWLClass)))

;; to run: M-x 'lein' 'test'

(defonce to
  (o/ontology :name "to"
              :iri "http://test"
              :prefix "test:"
              :noname true))

(deftest make-safe
  (is
   (p/make-safe "this is a test")
   "this_is_a_test"))

(deftest desc
  (let [ann (p/desc to "Test description")]
    (is
     (instance? OWLAnnotation ann))))

(deftest sio-class
  (is
   (instance? OWLClass
              (p/sio-class "name" "description"))))

(deftest defsclass
  (p/defsclass name "description")
  (is
   (instance? OWLClass name)))

(deftest sio-atom-annotation-maybe
  (let [without (p/sio-atom-annotation-maybe
                 to (o/owl-class to "without_chebi")
                 nil)
        with (p/sio-atom-annotation-maybe
              to (o/owl-class to "with_chebi")
              "CHEBI:00000")]
    (is
     (= 0
        (count
         (.getAnnotationAssertionAxioms to (.getIRI without)))))
    (is
     (= 1
        (count
         (.getAnnotationAssertionAxioms to (.getIRI with)))))))

(deftest sio-atom
  (is
   (instance? OWLClass (p/sio-atom "test_atom" nil))))

(deftest defsatom
  (p/defsatom name "description")
  (is
   (instance? OWLClass name)))

(deftest sio-oproperty
  (is
   (instance? OWLObjectProperty
              (p/sio-oproperty "name" "description"))))

(deftest defsoproperty
  (p/defsoproperty name "description")
  (is
   (instance? OWLObjectProperty name)))
