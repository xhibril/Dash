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



export function ValidateURL(url){

    if(!url) return "URL is required";

    const regex = /^(https?:\/\/)?([\w-]+\.)+[a-zA-Z]{2,}/;


    if(!regex.test(url)) return "Please enter a valid URL";
    return "VALID";

}


export function ValidateAlias(alias){

    if(alias.length < 4) return "Alias must be at least 5 characters";

    if(alias.length > 10) return "Alias must be less than 10 characters";
    
    if(ValidateInput(alias, "ALIAS") !== "VALID") return "Invalid alias";

    return "VALID";
}


function ValidateInput(input, type) {
    const allowedMap = {
        NAME: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ. ",
        EMAIL: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789._-+@",
        PASSWORD: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!.@#$%^&*",
        MESSAGE: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 .,!?;:'\"()[]{}-_/@#$%^&*+=|~`",
        ALIAS: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    };

    for (let c of input) {
        if (!allowedMap[type].includes(c)) return "INVALID";
    }

    return "VALID";

}