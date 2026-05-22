 //DES
function encryptByDES(message, key) {
		var keyHex = CryptoJS.enc.Utf8.parse(key);
		var encrypted = CryptoJS.DES.encrypt(message, keyHex, {
			mode: CryptoJS.mode.ECB,
			padding: CryptoJS.pad.Pkcs7
		});
		return encrypted.toString();
	}
//DES
function decryptByDES(ciphertext, key) {
	var keyHex = CryptoJS.enc.Utf8.parse(key);
	// direct decrypt ciphertext
	var decrypted = CryptoJS.DES.decrypt({
		ciphertext: CryptoJS.enc.Base64.parse(ciphertext)
	}, keyHex, {
		mode: CryptoJS.mode.ECB,
		padding: CryptoJS.pad.Pkcs7
	});
	return decrypted.toString(CryptoJS.enc.Utf8);
}
function userEncrypt(str){
	return str==""?"":encryptByDES(str,"12345678");
}