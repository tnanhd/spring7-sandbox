import type { AxiosError } from "axios";
import api from "./api";

export async function createMessage() {
  try {
    const response = await api.post("/api/messages", {
      headers: {
        "Content-Type": "application/json",
      },
    });

    return response.data;
  } catch (error) {
    const err = error as AxiosError;

    if (err.response?.status === 403) {
      throw new Error(
        "Forbidden: You don't have permission to create a message",
      );
    }

    throw err;
  }
}

export async function getMessage() {
  try {
    const response = await api.get("/api/messages");
    return response.data;
  } catch (error) {
    const err = error as AxiosError;

    if (err.response?.status === 403) {
      throw new Error(
        "Forbidden: You don't have permission to view messages",
      );
    }

    throw err;
  }
}
