describe("SSO", function() {
	var reloadTokenFormWasCalled = false;
    it("should not call 'reloadTokenForm' immediate", function() {
		expect(reloadTokenFormWasCalled).toEqual(false);
	});
    it("should trigger ajax and call 'reloadTokenForm'", function() {
    	setTimeout(function(){expect(reloadTokenFormWasCalled).toEqual(true);}, 11000);
	});
    it("should stop the timer when idle", function() {
    	stopRenew();
    	setTimeout(function(){expect(reloadTokenFormWasCalled).toEqual(true);}, 11000);
	});
});
