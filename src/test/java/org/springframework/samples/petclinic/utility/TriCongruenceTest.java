package org.springframework.samples.petclinic.utility;

import com.github.mryf323.tractatus.*;
import com.github.mryf323.tractatus.experimental.extensions.ReportingExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(ReportingExtension.class)
@ClauseDefinition(clause = 'a', def = "t1arr[0] < 0")
@ClauseDefinition(clause = 'b', def = "t1arr[0] + t1arr[1] < t1arr[2]")
@ClauseDefinition(clause = 'c', def = "t1arr[0] != t2arr[0]")
@ClauseDefinition(clause = 'd', def = "t1arr[1] != t2arr[1]")
@ClauseDefinition(clause = 'e', def = "t1arr[2] != t2arr[2]")
class TriCongruenceTest {

	private static final Logger log = LoggerFactory.getLogger(TriCongruenceTest.class);

	@UniqueTruePoint(
		predicate = "c + d + e",
		dnf = "c + d + e",
		implicant = "c",
		valuations = {
			@Valuation(clause = 'c',valuation = true),
			@Valuation(clause = 'd',valuation = false),
			@Valuation(clause = 'e',valuation = false)
		}
	)
	@Test
	public void notCongruentIfSmallestSidesDifferentOtherSidesEqual(){
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(3, 1, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@UniqueTruePoint(
		predicate = "c + d + e",
		dnf = "c + d + e",
		implicant = "d",
		valuations = {
			@Valuation(clause = 'c',valuation = false),
			@Valuation(clause = 'd',valuation = true),
			@Valuation(clause = 'e',valuation = false)
		}
	)
	@Test
	public void notCongruentIfMiddleSidesDifferentOtherSidesEqual(){
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(4, 2, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@UniqueTruePoint(
		predicate = "c + d + e",
		dnf = "c + d + e",
		implicant = "e",
		valuations = {
			@Valuation(clause = 'c',valuation = false),
			@Valuation(clause = 'd',valuation = false),
			@Valuation(clause = 'e',valuation = true)
		}
	)
	@Test
	public void notCongruentIfLargestSidesDifferentOtherSidesEqual(){
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(3, 2, 4);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@NearFalsePoint(
		predicate = "c + d + e",
		dnf = "c + d + e",
		implicant = "c",
		clause = 'c',
		valuations = {
			@Valuation(clause = 'c',valuation = false),
			@Valuation(clause = 'd',valuation = false),
			@Valuation(clause = 'e',valuation = false)
		}
	)
	@ClauseCoverage(
		predicate = "a + b",
		valuations = {
			@Valuation(clause = 'a',valuation = false),
			@Valuation(clause = 'b',valuation = true)
		})
	@CACC(
		predicate = "a + b",
		majorClause = 'b',
		valuations = {
			@Valuation(clause = 'a',valuation = false),
			@Valuation(clause = 'b',valuation = true)
		},
		predicateValue = true
	)
	@Test
	public void notCongruentIfSidesAreEqualAndSumOfTwoSidesLessThanLargestSide() {
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(3, 2, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	@NearFalsePoint(
		predicate = "c + d + e",
		dnf = "c + d + e",
		implicant = "d",
		clause = 'd',
		valuations = {
			@Valuation(clause = 'c',valuation = false),
			@Valuation(clause = 'd',valuation = false),
			@Valuation(clause = 'e',valuation = false)
		}
	)
	@CACC(
		predicate = "a + b",
		majorClause = 'a',/*and 'b'*/
		valuations = {
			@Valuation(clause = 'a',valuation = false),
			@Valuation(clause = 'b',valuation = false)
		},
		predicateValue = false
	)
	@ClauseCoverage(
		predicate = "a + b",
		valuations = {
			@Valuation(clause = 'a',valuation = false),
			@Valuation(clause = 'b',valuation = false)
	})/*
	@CACC(
		predicate = "a + b",
		majorClause = 'b',
		valuations = {
			@Valuation(clause = 'a',valuation = false),
			@Valuation(clause = 'b',valuation = false)
		},
		predicateValue = false
	)*/
	@Test
	public void congruentIfSidesAreEqualAndLargestSideLessThanSumOfOtherSides() {
		Triangle t1 = new Triangle(2, 3, 4);
		Triangle t2 = new Triangle(4, 2, 3);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	/*infeasible
	a: true
	b: false

	t1arr[0] < 0
    t1arr[0] + t1arr[1] > t1arr[2]

	(t1arr[1] <= t1arr[2]) and (t1arr[0] < 0 ) =>  t1arr[0] + t1arr[1] < t1arr[2]


	@ClauseCoverage(
		predicate = "a + b",
		valuations = {
			@Valuation(clause = 'a',valuation = true),
			@Valuation(clause = 'b',valuation = false)
		})
	@CACC(
		predicate = "a + b",
		majorClause = 'a',
		valuations = {
			@Valuation(clause = 'a',valuation = true),
			@Valuation(clause = 'b',valuation = false)
		},
		predicateValue = true
	)*/

	@NearFalsePoint(
		predicate = "c + d + e",
		dnf = "c + d + e",
		implicant = "e",
		clause = 'e',
		valuations = {
			@Valuation(clause = 'c',valuation = false),
			@Valuation(clause = 'd',valuation = false),
			@Valuation(clause = 'e',valuation = false)
		}
	)
	@ClauseCoverage(
		predicate = "a + b",
		valuations = {
			@Valuation(clause = 'a',valuation = true),
			@Valuation(clause = 'b',valuation = true)
		})
	@Test
	public void notCongruentIfSidesAreEqualAndSmallestSideIsNegative() {
		Triangle t1 = new Triangle(-1, 3, 4);
		Triangle t2 = new Triangle(4, -1, 3);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/**
	 * 	  f = ab + bc

	 * UTPC :
	 ¬f = ¬b + ¬a¬c

	 {ab} : {TTF}
	 {bc} : {FTT}
	 {¬b} : {TFT,TFF,FFT}
	 {¬a¬c} : {FTF}

	 CUTPNFP :
	 for clause a, we can pair unique true point TTF with corresponding near false point FTF
	 for clause b, we can pair unique true point TTF with corresponding near false point TFF

	 for clause b, we can pair unique true point FTT with corresponding near false point FFT
	 for clause c, we can pair unique true point FTT with corresponding near false point FTF

	 CUTPNFP set : {TTF,FTT,FTF,TFF,FFT}

	 CUTPNFP set doesn't contain TFT => CUTPNFP doesn't subsume UTPC
	 */
	private static boolean questionTwo(boolean a, boolean b, boolean c, boolean d, boolean e) {
		boolean predicate = a && b || b && c ;
//		predicate = a predicate with any number of clauses
		return predicate;
	}
}
