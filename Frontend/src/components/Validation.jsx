export function ValidatePassword(password) {

    const minLength = 8;
    const maxLength = 32;

    if (password.length < minLength) return "Password must be longer than 8 characters.";
    if (password.length > maxLength) return "Password must be shorter than 32 characters.";


    if(ValidateInput(password, "PASSWORD") !== "VALID"){
        return "Invalid Input";
    }
    
    const regex = /^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*.]).+$/;

    if (!regex.test(password)) {
        return "Password must be 8–32 characters and include at least one uppercase letter, " +
            "one number, and one special character (.!@#$%^&*).";
    }


    return "VALID";
};


export function ValidateEmail(email) {
    email = email.trim();

      if(ValidateInput(email, "EMAIL") !== "VALID"){
        return "Invalid Input";
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(email)) return "Please enter a valid email";

    return "VALID";
};


export function ValidateCode(code, maxDigitsAllowed) {

    const regex = /^[0-9]+$/;

    if (code.length > maxDigitsAllowed) return "Digit limit exceeded";

    if (!regex.test(code)) return "Code must only contain digits";

    return "VALID";
};



function ValidateInput(input, type) {
    const allowedMap = {
        NAME: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ. ",
        EMAIL: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789._-+@",
        PASSWORD: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!.@#$%^&*",
        MESSAGE: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 .,!?;:'\"()[]{}-_/@#$%^&*+=|~`"
    };

    for (let c of input) {
        if (!allowedMap[type].includes(c)) return "INVALID";
    }

    return "VALID";

}