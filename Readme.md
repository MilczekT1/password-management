{
    "Account": {
        "id": 2,
        "firstName": "kon",
        "lastName": "bon",
        "email": "test@mail.com"
    },
    "ResetCode": "29431ce1-8282-4489-8dd9-50f91e4c5653"
}

ObjectNode accountNode = new ObjectMapper().createObjectNode();

accountNode.put("id", id);

accountNode.put("firstName", firstName);

accountNode.put("lastName", lastName);

accountNode.put("email", email);

ObjectNode json = new ObjectMapper().createObjectNode();

json.set("Account", accountNode);

json.put("ResetCode", resetCode);