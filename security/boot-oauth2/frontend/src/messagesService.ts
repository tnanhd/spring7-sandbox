import { api } from "./api";

export const fetchMessages = async (accessToken: string) => {
  const response = await api.get("/api/messages", {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });
  return response.data;
};