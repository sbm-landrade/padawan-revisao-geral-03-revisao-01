export interface Team {
  id: number;
  teamName: string;
  country: string;
  coachName: string;
  teamValue: number;
  createdAt: string;//A data e hora em que o time foi criado, representada como uma string.
  updatedAt: string | null;//A data e hora da última atualização do time. Pode ser null se o time ainda não tiver sido atualizado
}
