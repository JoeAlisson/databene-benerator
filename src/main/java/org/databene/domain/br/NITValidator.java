/*
 * (c) Copyright 2009-2010 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.databene.domain.br;

import org.databene.commons.MathUtil;
import org.databene.commons.validator.bean.AbstractConstraintValidator;

import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Verifies Brazilian NIT numbers.
 * <br/><br/>
 * @since 0.7.7
 * @author JoeAlisson
 */
public class NITValidator extends AbstractConstraintValidator<NIT, String> {

	private static final Pattern pattern = Pattern.compile("\\d{3}\\.\\d{5}\\.\\d{2}-\\d");

	private boolean acceptingFormattedNumbers;

	public NITValidator() {
	    this(false);
    }

	public NITValidator(boolean acceptingFormattedNumbers) {
	    this.acceptingFormattedNumbers = acceptingFormattedNumbers;
    }

	public boolean isAcceptingFormattedNumbers() {
    	return acceptingFormattedNumbers;
    }

	public void setAcceptingFormattedNumbers(boolean acceptingFormattedNumbers) {
    	this.acceptingFormattedNumbers = acceptingFormattedNumbers;
    }
	
	@Override
	public void initialize(NIT params) {
	    super.initialize(params);
	    acceptingFormattedNumbers = params.formatted();
	}
	
	public boolean isValid(String number, ConstraintValidatorContext context) {
		// do simple checks first
		if (number == null)
			return false;
		
		if (number.length() == 14)
			if (acceptingFormattedNumbers && pattern.matcher(number).matches())
				number = number.substring(0, 3) + number.substring(4, 9) + number.substring(10, 12) + number.substring(13, 14);
			else
				return false;
			
		if (number.length() != 11)
			return false;

		int v1 = MathUtil.weightedSumOfDigits(number, 0, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2);
		v1 = 11 - (v1 % 11);
		if (v1 >= 10)
			v1 = 0;

		return (v1 == number.charAt(10) - '0');
    }
	
}
