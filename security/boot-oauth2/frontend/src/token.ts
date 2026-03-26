export const getAccessToken = async (code: string, codeVerifier: string) => {
  const params = new URLSearchParams({
    client_id: "myclient",
    redirect_uri: "http://localhost:8080",
    code: code,
    code_verifier: codeVerifier,
    grant_type: "authorization_code",
  });
  const response = await fetch(
    "http://localhost:9000/realms/myrealm/protocol/openid-connect/token",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: params.toString(),
    },
  );

  return await response.json();
};
