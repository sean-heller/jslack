export interface GroupsCreateChildResponse {
    ok?:       boolean;
    group?:    Group;
    error?:    string;
    needed?:   string;
    provided?: string;
}

export interface Group {
    id?:                   string;
    name?:                 string;
    is_group?:             boolean;
    created?:              number;
    creator?:              string;
    is_archived?:          boolean;
    name_normalized?:      string;
    is_mpim?:              boolean;
    parent_group?:         string;
    is_open?:              boolean;
    last_read?:            string;
    latest?:               Latest;
    unread_count?:         number;
    unread_count_display?: number;
    members?:              string[];
    topic?:                Purpose;
    purpose?:              Purpose;
}

export interface Latest {
    user?:    string;
    type?:    string;
    subtype?: string;
    ts?:      string;
    text?:    string;
}

export interface Purpose {
    value?:    string;
    creator?:  string;
    last_set?: number;
}
