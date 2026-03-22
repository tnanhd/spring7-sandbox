import api from "./api";

export async function login(username: string, password: string) {
  const csrfResponse = await api.get("/login");
  if (csrfResponse.status !== 200) {
    throw new Error("Failed to fetch CSRF token");
  }

  const loginResponse = await api.post(
    "/login",
    { username, password },
    {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      validateStatus: () => true,
    },
  );

  if (loginResponse.status !== 200) {
    throw new Error("Invalid username or password");
  }
}

export async function logout() {
  await api.post("/logout");
}