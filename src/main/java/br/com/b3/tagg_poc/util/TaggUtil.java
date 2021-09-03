package br.com.b3.tagg_poc.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TaggUtil {

	public static List<Integer> generateRandomIntegers(int randomNumberOrigin, int randomNumberBound, int maxSize, boolean distinct) {
		if (distinct) {
			return new Random()
					.ints(randomNumberOrigin, randomNumberBound)
					.distinct()
					.limit(maxSize)
					.boxed().
					collect(Collectors.toList());
		} else {
			return new Random()
					.ints(randomNumberOrigin, randomNumberBound)
					.limit(maxSize)
					.boxed().
					collect(Collectors.toList());
		}
	}
	
	public static Integer generateRandomInteger(int randomNumberOrigin, int randomNumberBound) {
		return generateRandomIntegers(randomNumberOrigin, randomNumberBound, 1, false).get(0);
	}

	public static LocalDateTime generateRandomLocalDateTime() {
		LocalDateTime reference = LocalDateTime.now();
		int minute = reference.getMinute();
		List<Integer> minutes = null;

		if (minute == 59) {
			minutes = Arrays.asList(minute - 2, minute - 1 , minute);
		} else if (minute == 58) {
			minutes = Arrays.asList(minute - 1, minute , minute + 1);
		} else {
			minutes = Arrays.asList(minute, minute + 1 , minute + 2);
		}

		try {
			int monthIndex = 0;
			monthIndex = generateRandomIntegers(0, 3, 1, false).get(0);
			minute = minutes.get(monthIndex);
		}catch (Exception e) {
		}

		return LocalDateTime.of(reference.getYear(), reference.getMonth(), reference.getDayOfMonth(), reference.getHour(), minute , generateRandomInteger(0, 60));
	}

	public static BigDecimal generateRandomBigDecimalFromRange(BigDecimal min, BigDecimal max) {
		BigDecimal randomBigDecimal = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
		return randomBigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);
	}

}
