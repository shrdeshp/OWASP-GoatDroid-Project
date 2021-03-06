/**
 * OWASP GoatDroid Project
 * 
 * This file is part of the Open Web Application Security Project (OWASP)
 * GoatDroid project. For details, please see
 * https://www.owasp.org/index.php/Projects/OWASP_GoatDroid_Project
 *
 * Copyright (c) 2012 - The OWASP Foundation
 * 
 * GoatDroid is published by OWASP under the GPLv3 license. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 * 
 * @author Jack Mannino (Jack.Mannino@owasp.org https://www.owasp.org/index.php/User:Jack_Mannino)
 * @created 2012
 */
package org.owasp.goatdroid.webservice.herdfinancial.controllers;

import org.owasp.goatdroid.webservice.herdfinancial.model.BaseModel;
import org.owasp.goatdroid.webservice.herdfinancial.services.HFForgotPasswordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "herdfinancial/api/v1/pub/forgot_password", produces = "application/json")
public class HFForgotPasswordController {

	@Autowired
	HFForgotPasswordServiceImpl forgotPasswordService;

	@RequestMapping(value = "request-code", method = RequestMethod.POST)
	@ResponseBody
	public BaseModel requestCode(
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "secretQuestionIndex", required = true) int secretQuestionIndex,
			@RequestParam(value = "secretQuestionAnswer", required = true) String secretQuestionAnswer) {
		try {
			return forgotPasswordService.requestCode(userName,
					secretQuestionIndex, secretQuestionAnswer);
		} catch (NullPointerException e) {
			BaseModel base = new BaseModel();
			base.setSuccess(false);
			return base;
		}
	}

	@RequestMapping(value = "verify-code", method = RequestMethod.POST)
	@ResponseBody
	public BaseModel verifyCode(
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "passwordResetCode", required = true) int passwordResetCode) {
		try {
			return forgotPasswordService
					.verifyCode(userName, passwordResetCode);
		} catch (NullPointerException e) {
			BaseModel base = new BaseModel();
			base.setSuccess(false);
			return base;
		}
	}

	@RequestMapping(value = "update-password", method = RequestMethod.POST)
	@ResponseBody
	public BaseModel getBalances(
			@RequestParam(value = "userName") String userName,
			@RequestParam(value = "passwordResetCode") int passwordResetCode,
			@RequestParam(value = "password") String password) {
		try {
			return forgotPasswordService.updatePassword(userName,
					passwordResetCode, password);
		} catch (NullPointerException e) {
			BaseModel base = new BaseModel();
			base.setSuccess(false);
			return base;
		}
	}
}
